package Task1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MovieTests {

    private LocalTime startMatineeTime;
    private LocalTime endMatineeTime;
    private MovieTicketPriceCalculator calculator;

    @BeforeEach
    public void setUp() {
        startMatineeTime = LocalTime.of(12, 0);
        endMatineeTime = LocalTime.of(17, 0);
        calculator = new MovieTicketPriceCalculator(startMatineeTime, endMatineeTime, 12, 65);
    }

    @Test
    public void testConstructor_StartTimeAfterEndTime_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new MovieTicketPriceCalculator(LocalTime.of(18, 0), LocalTime.of(17, 0), 12, 65)
        );
    }

    @Test
    public void testComputeDiscount_NoDiscount() {
        int discount = calculator.computeDiscount(30); // Age that doesn't qualify for any discount
        assertEquals(0, discount, "Expected no discount for an adult age that does not meet child or senior criteria.");
    }

    @Test
    public void testComputeDiscount_ChildDiscount() {
        int discount = calculator.computeDiscount(12); // Maximum child age for discount
        assertEquals(300, discount, "Expected child discount for the maximum child age.");
    }

    @Test
    public void testComputeDiscount_SeniorDiscount() {
        int discount = calculator.computeDiscount(65); // Minimum senior age for discount
        assertEquals(400, discount, "Expected senior discount for the minimum senior age.");
    }

    @Test
    public void testComputeDiscount_BelowChildAgeThreshold() {
        int discount = calculator.computeDiscount(5); // Below maximum child age
        assertEquals(300, discount, "Expected child discount for ages below the child age threshold.");
    }

    @Test
    public void testComputeDiscount_AboveSeniorAgeThreshold() {
        int discount = calculator.computeDiscount(80); // Above minimum senior age
        assertEquals(400, discount, "Expected senior discount for ages above the senior age threshold.");
    }

    @Test
    public void testComputeDiscount_YoungAdult() {
        int discount = calculator.computeDiscount(20); // Age between child and senior thresholds
        assertEquals(0, discount, "Expected no discount for ages that do not qualify as child or senior.");
    }

    @Test
    public void testComputeDiscount_JustBelowSeniorAge() {
        int discount = calculator.computeDiscount(64); // Just below the senior discount age
        assertEquals(0, discount, "Expected no discount for ages just below the senior discount age.");
    }

    @Test
    public void testComputeDiscount_JustAboveChildAge() {
        int discount = calculator.computeDiscount(13); // Just above the child discount age
        assertEquals(0, discount, "Expected no discount for ages just above the child discount age.");
    }

    @Test
    public void testComputePrice_StandardTimeAdult() {
        int price = calculator.computePrice(LocalTime.of(18, 0), 30);
        assertEquals(2700, price);
    }

    @Test
    public void testComputePrice_MatineeTimeAdult() {
        int price = calculator.computePrice(LocalTime.of(13, 0), 30);
        assertEquals(2400, price);
    }

    @Test
    public void testComputePrice_StandardTimeChild() {
        int price = calculator.computePrice(LocalTime.of(20, 0), 10);
        assertEquals(2400, price);
    }

    @Test
    public void testComputePrice_MatineeTimeChild() {
        int price = calculator.computePrice(LocalTime.of(14, 0), 10);
        assertEquals(2100, price);
    }

    @Test
    public void testComputePrice_StandardTimeSenior() {
        int price = calculator.computePrice(LocalTime.of(19, 0), 70);
        assertEquals(2300, price);
    }

    @Test
    public void testComputePrice_MatineeTimeSenior() {
        int price = calculator.computePrice(LocalTime.of(15, 0), 70);
        assertEquals(2000, price);
    }

    @Test
    public void testComputePrice_BoundaryAtStartMatinee() {
        int price = calculator.computePrice(startMatineeTime, 30);
        assertEquals(2400, price);
    }

    @Test
    public void testComputePrice_BoundaryJustBeforeEndMatinee() {
        int price = calculator.computePrice(endMatineeTime.minusMinutes(1), 30);
        assertEquals(2400, price);
    }

    @Test
    public void testComputePrice_BoundaryAtEndMatinee() {
        int price = calculator.computePrice(endMatineeTime, 30);
        assertEquals(2700, price);
    }

    @Test
    public void testComputePrice_ExactChildAge() {
        int price = calculator.computePrice(LocalTime.of(14, 0), 12);
        assertEquals(2100, price);
    }

    @Test
    public void testComputePrice_ExactSeniorAge() {
        int price = calculator.computePrice(LocalTime.of(14, 0), 65);
        assertEquals(2000, price);
    }
}
