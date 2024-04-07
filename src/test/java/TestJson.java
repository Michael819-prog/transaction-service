import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.Transaction;
import org.example.PaymentService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJson {

    @Test
    public void testJsonGeneration() {
        // create transaction
        Transaction transaction = new Transaction("1000000000000000", "01", 1000, 978);
        String errorMsg = "";
        String maskedPan = transaction.getPan().substring(0, 6) + "******" + transaction.getPan().substring(12);

        // generate JSON
        String json = PaymentService.testJson(transaction, errorMsg);

        // parse JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject data = jsonObject.getAsJsonObject("data");
        JsonObject responseData = data.getAsJsonObject("transaction");

        // check content
        assertEquals(maskedPan, responseData.get("pan").getAsString());
        assertEquals(transaction.getType(), responseData.get("type").getAsString());
        assertEquals(transaction.getAmount(), responseData.get("amount").getAsInt());
        assertEquals(transaction.getCurrency(), responseData.get("currency").getAsInt());
        assertEquals(transaction.getStatus(), responseData.get("status").getAsString());
    }

    @Test
    public void testJsonGenerationWithOutOfRangeError() {
        // create transaction
        Transaction transaction = new Transaction("1000000000000000", "01", 20000, 978);
        String errorMsg = "Transaction amount is out of range!";

        // generate JSON
        String json = PaymentService.testJson(transaction, errorMsg);

        // parse JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // check content for error message
        JsonObject error = jsonObject.getAsJsonObject("error");
        assertEquals(errorMsg, error.get("message").getAsString());
        assertEquals(transaction.getStatus(), error.get("status").getAsString());
    }

    @Test
    public void testJsonGenerationWithPanError() {
        // create transaction
        Transaction transaction = new Transaction("8000000000000000", "01", 10, 978);
        String errorMsg = "Pan is out of range!";

        // generate JSON
        String json = PaymentService.testJson(transaction, errorMsg);

        // parse JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // check content for error message
        JsonObject error = jsonObject.getAsJsonObject("error");
        assertEquals(errorMsg, error.get("message").getAsString());
        assertEquals(transaction.getStatus(), error.get("status").getAsString());
    }

    @Test
    public void testJsonGenerationWithLongPanError() {
        // create transaction
        Transaction transaction = new Transaction("8888888888888888000000000000000", "01", 10, 978);
        String errorMsg = "Pan is out of range!";

        // generate JSON
        String json = PaymentService.testJson(transaction, errorMsg);

        // parse JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // check content for error message
        JsonObject error = jsonObject.getAsJsonObject("error");
        assertEquals(errorMsg, error.get("message").getAsString());
        assertEquals(transaction.getStatus(), error.get("status").getAsString());
    }

    @Test
    public void testJsonGenerationWithShortPanError() {
        // create transaction
        Transaction transaction = new Transaction("00", "01", 10, 978);
        String errorMsg = "Pan is out of range!";

        // generate JSON
        String json = PaymentService.testJson(transaction, errorMsg);

        // parse JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // check content for error message
        JsonObject error = jsonObject.getAsJsonObject("error");
        assertEquals(errorMsg, error.get("message").getAsString());
        assertEquals(transaction.getStatus(), error.get("status").getAsString());
    }
}

