import org.example.PaymentService;
import org.example.Transaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void testApprovedTransaction() {
        Transaction transaction = new Transaction("1000000000000000", "01", 1000, 840);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("01", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransaction() {
        Transaction transaction = new Transaction("2000000000000000", "01", 6000, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionShortPan() {
        Transaction transaction = new Transaction("22", "01", 6000, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionMiddlePan() {
        Transaction transaction = new Transaction("21364578", "01", 6000, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testApprovedTransactionSecond() {
        Transaction transaction = new Transaction("2000000000000000", "02", 100, 840);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("01", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionInvalidPAN() {
        Transaction transaction = new Transaction("4000000000000000", "02", 2000, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionSecondInvalidType() {
        Transaction transaction = new Transaction("2000000000000000", "08", 100, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionMaxAmount() {
        Transaction transaction = new Transaction("2000000000000000", "02", 10100, 840);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testApprovedTransactionThird() {
        Transaction transaction = new Transaction("3000000000000000", "03", 100, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("01", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionInvalidType() {
        Transaction transaction = new Transaction("3000000000000000", "04", 100, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionThirdInvalidPAN() {
        Transaction transaction = new Transaction("4000000000000000", "03", 2000, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }

    @Test
    void testDeclinedTransactionThirdInvalidMaxAmount() {
        Transaction transaction = new Transaction("3000000000000000", "03", 2001, 840);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        assertEquals("02", processedTransaction.getStatus());
    }
}
