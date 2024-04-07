import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.ErrorMessage;
import org.example.Transaction;
import org.example.PaymentService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    @Test
    public void testJsonGeneration_validPanType01_validJsonData() {
        // arrange
        Transaction transaction = new Transaction("1000000000000000", "01", 1000, 978);
        String maskedPan = transaction.getPan().substring(0, 6) + "******" + transaction.getPan().substring(12);

        // act
        JsonObject response = invokeJsonGenerator(transaction, ErrorMessage.EMPTY_ERROR_MSG.message, false);

        // assert
        assertEquals(maskedPan, response.get("pan").getAsString());
        assertEquals(transaction.getType(), response.get("type").getAsString());
        assertEquals(transaction.getAmount(), response.get("amount").getAsInt());
        assertEquals(transaction.getCurrency(), response.get("currency").getAsInt());
        assertEquals(transaction.getStatus(), response.get("status").getAsString());
    }

    @Test
    public void testJsonGenerationWithOutOfRangeError_invalidAmountType01_errorMessage() {
        // arrange
        Transaction transaction = new Transaction("1000000000000000", "01", 20000, 978);

        // act
        JsonObject response = invokeJsonGenerator(transaction, ErrorMessage.TRANS_AMOUNT_OUT_OF_RANGE.message, true);

        // assert
        assertEquals(ErrorMessage.TRANS_AMOUNT_OUT_OF_RANGE.message, response.get("message").getAsString());
        assertEquals(transaction.getStatus(), response.get("status").getAsString());
    }

    @Test
    public void testJsonGenerationWithPanError_invalidPanType01_errorMessage() {
        // arrange
        Transaction transaction = new Transaction("8000000000000000", "01", 10, 978);

        // act
        JsonObject response = invokeJsonGenerator(transaction, ErrorMessage.PAN_OUT_OF_RANGE.message, true);

        // assert
        assertEquals(ErrorMessage.PAN_OUT_OF_RANGE.message, response.get("message").getAsString());
        assertEquals(transaction.getStatus(), response.get("status").getAsString());
    }

    @Test
    public void testJsonGenerationWithLongPanError_invalidLongPanType01_errorMessage() {
        // arrange
        Transaction transaction = new Transaction("8888888888888888000000000000000", "01", 10, 978);

        // act
        JsonObject response = invokeJsonGenerator(transaction, ErrorMessage.PAN_OUT_OF_RANGE.message, true);

        // assert
        assertEquals(ErrorMessage.PAN_OUT_OF_RANGE.message, response.get("message").getAsString());
        assertEquals(transaction.getStatus(), response.get("status").getAsString());
    }

    @Test
    public void testJsonGenerationWithShortPanError_invalidShortPanType01_errorMessage() {
        // arrange
        Transaction transaction = new Transaction("00", "01", 10, 978);

        // act
        JsonObject response = invokeJsonGenerator(transaction, ErrorMessage.PAN_OUT_OF_RANGE.message, true);

        // assert
        assertEquals(ErrorMessage.PAN_OUT_OF_RANGE.message, response.get("message").getAsString());
        assertEquals(transaction.getStatus(), response.get("status").getAsString());
    }

    private JsonObject invokeJsonGenerator(Transaction transaction, String errorMsg, Boolean isError) {
        String json = PaymentService.testJson(transaction, errorMsg);

        // parse JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        if (isError) {
            return jsonObject.getAsJsonObject("error");
        }
        JsonObject data = jsonObject.getAsJsonObject("data");
        JsonObject responseData = data.getAsJsonObject("transaction");

        return responseData;
    }
}

