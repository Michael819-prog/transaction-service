import org.example.PaymentService;
import org.example.Transaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void testApprovedTransaction_validPanType01_approvedStatus() {
        // arrange
        Transaction transaction = new Transaction("1000000000000000", "01", 1000, 840);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("01", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransaction_invalidPanType01_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("2000000000000000", "01", 6000, 978);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionShortPan_shortPanType01_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("22", "01", 6000, 978);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionMiddlePan_middlePanType01_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("21364578", "01", 6000, 978);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testApprovedTransactionSecond_validPanType02_approvedStatus() {
        // arrange
        Transaction transaction = new Transaction("2000000000000000", "02", 100, 840);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("01", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionInvalidPAN_invalidPanRangeType02_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("4000000000000000", "02", 2000, 978);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionSecondInvalidType_invalidPanTypeType02_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("2000000000000000", "08", 100, 978);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionMaxAmount_invalidPanAmountType02_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("2000000000000000", "02", 10100, 840);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testApprovedTransactionThird_validPanType03_approvedStatus() {
        // arrange
        Transaction transaction = new Transaction("3000000000000000", "03", 100, 978);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("01", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionInvalidType_invalidTypeType03_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("3000000000000000", "04", 100, 978);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionThirdInvalidPAN_invalidPanType03_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("4000000000000000", "03", 2000, 978);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionThirdInvalidMaxAmount_invalidAmountType03_declinedStatus() {
        // arrange
        Transaction transaction = new Transaction("3000000000000000", "03", 2001, 840);

        // act
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);

        // assert
        assertEquals("02", processedTransaction.getStatus());
    }
}
