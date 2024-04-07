package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.Operation;

@Service
public class PaymentService {

    @Operation(summary = "Process transaction and return .log file", description = "Returns a log file with JSON response")
    public static Transaction PaymentServiceProcessTransaction(Transaction transaction) {
        String nameOfFile = "payment-service_" + new SimpleDateFormat("yyMMdd").format(new Date()) + ".log";
        String errorMsg = "";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nameOfFile, true))) {
            // hide PAN (masking)
            String hiddenPan = maskPAN(transaction.getPan());

            // check PAN range and amount
            errorMsg = checkPanRangeAndAmount(transaction, errorMsg);

            // .log file details filling
            // 1 -> timestamp, 2 -> info or error text, 3 -> PAN with mask, 4 -> current date, 5 -> amount of money (double),
            // 6 -> currency (alphabetic), 7 -> status
            String logFileContent = String.format("%s %s with card number %s on %s, amount %.2f %s, status %s. Return transaction details: %s",
                    getTimeStamp(), errorMsg.equals("") ? "INFO: Processed transaction" : "ERROR: Problem in transaction", hiddenPan,
                    new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()), transaction.getAmount() / 100.0,
                    getCurrencyNameByCode(transaction.getCurrency()), getStatusNameByCode(transaction.getStatus()), outputToJson(transaction, errorMsg));

            // Output log file
            writer.println(logFileContent);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage()); // print exception msg
        }

        return transaction;

    }

    // Helper function to check PAN range and allowed amount
    private static String checkPanRangeAndAmount(Transaction transaction, String errorMsg) {
        // Check range first
        if (!checkPanRange(transaction.getPan(), transaction.getType())) {
            transaction.setStatus("02"); // decline
            transaction.setStatusName(getStatusNameByCode("02"));
            errorMsg = "Pan is out of range!";
        }
        // checking max amount for APP, BRW and 3RI
        // 1 unit == 100 subunits, multiply by 100
        else if ((transaction.getType().equals("01") && transaction.getAmount() > 5000) ||
                (transaction.getType().equals("02") && transaction.getAmount() > 10000) ||
                (transaction.getType().equals("03") && transaction.getAmount() > 2000)) {

            transaction.setStatus("02"); // decline
            transaction.setStatusName(getStatusNameByCode("02"));
            errorMsg = "Transaction amount is out of range!";
        } else {
            transaction.setStatus("01"); // approve
            transaction.setStatusName(getStatusNameByCode("01"));
            errorMsg = "";
        }

        return errorMsg;
    }

    // Helper function to check PAN range
    private static boolean checkPanRange(String pan, String type) {
        // check PAN length is <= 16
        if (pan.length() > 16)
            return false;

        Long panNumber = Long.parseLong(pan);

        switch (type) {
            case "01": // APP
                return panNumber >= PanRange.APP_MIN.value && panNumber <= PanRange.APP_MAX.value;
            case "02": // BRW
                return panNumber >= PanRange.BRW_MIN.value && panNumber <= PanRange.BRW_MAX.value;
            case "03": // 3RI
                return panNumber >= PanRange.MIN_3RI.value && panNumber <= PanRange.MAX_3RI.value;
            default:
                return false;
        }
    }

    // Helper function to output correct currency
    private static String getStatusNameByCode(String status) {
        switch (status) {
            case "00":
                return "Active";
            case "01":
                return "Approved";
            case "02":
                return "Declined";
            default:
                return "Unknown";
        }
    }

    // Helper function to output correct currency
    private static String getCurrencyNameByCode(Integer currency) {
        switch (currency) {
            case 840:
                return "usd";
            case 978:
                return "eur";
            default:
                return "unknown";
        }
    }

    // Fore testing purposes only
    public static String testJson(Transaction transaction, String errorMsg) {
        errorMsg = checkPanRangeAndAmount(transaction, errorMsg);
        return outputToJson(transaction, errorMsg);
    }

    // Helper function to generate JSON output
    private static String outputToJson(Transaction transaction, String errorMsg) {
        // if error -> show it in the JSON as error msg
        if (!errorMsg.equals("")) {
            return "{\"error\":{\"message\":\"" + errorMsg + "\",\"status\":\"" + transaction.getStatus() + "\"}}";
        }

        // hide Pan with mask
        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create(); // hide transient fields
        String jsonString = gson.toJson(transaction);
        JsonElement je = new Gson().fromJson(jsonString, JsonElement.class);
        JsonObject jo = je.getAsJsonObject();
        String maskedPan = maskPAN(transaction.getPan());
        jo.addProperty("pan", maskedPan);

        // return via JSON response
        JsonObject responseData = new JsonObject();
        responseData.add("transaction", jo);
        JsonObject data = new JsonObject();
        data.add("data", responseData);

        return new Gson().toJson(data);

        // hardcoded alternative
        /*return "{\"data\":{\"transaction\":{\"pan\":\"" + maskPAN(transaction.getPan()) +
                "\",\"type\":\"" + transaction.getType() + "\",\"amount\":\"" + transaction.getAmount() +
                "\",\"currency\":\"" + transaction.getCurrency() + "\",\"status\":\"" + transaction.getStatus() + "\"}}}";*/
    }

    // Helper function to mask PAN with stars
    private static String maskPAN(String pan) {
        int length = pan.length();

        // length <= 6 -> hide 1 or hide all
        if (length <= 1) {
            return pan.replaceAll(".", "*");
        }
        else if (length <= 6) {
            return pan.substring(0, 1) + "*" + pan.substring(2);
        }
        // length until 16 -> hide proportionally
        else if (length < 16) {
            int visibleLength = 4; // visible characters in the PAN
            int hiddenLength = length - visibleLength - 2; // characters to hide

            // masked PAN with stars in the middle
            StringBuilder maskedPanBuilder = new StringBuilder();
            maskedPanBuilder.append(pan, 0, visibleLength);
            for (int i = 0; i < hiddenLength; i++) {
                maskedPanBuilder.append("*");
            }
            maskedPanBuilder.append(pan.substring(visibleLength + hiddenLength));
            return maskedPanBuilder.toString();
        } else {
            // Length is greater than 16, apply the original logic
            return pan.substring(0, 6) + "******" + pan.substring(12);
        }
    }

    // Helper function to return date as string
    private static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

}
