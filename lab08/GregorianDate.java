public class GregorianDate extends Date {

    private static final int[] MONTH_LENGTHS = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public GregorianDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }


    // YOUR CODE HERE

    @Override
    public Date nextDate() {
        int newDay = dayOfMonth + 1;
        int newMonth = month;
        int newYear = year;

        if (newDay > MONTH_LENGTHS[month - 1]) {
            newDay = 1;
            newMonth += 1;
        }

        if (newMonth > MONTH_LENGTHS.length) {
            newMonth = 1;
            newYear += 1;
        }
        return new GregorianDate(newYear, newMonth, newDay);
    }

    @Override
    public int dayOfYear() {
        int precedingMonthDays = 0;
        for (int m = 1; m < month; m += 1) {
            precedingMonthDays += getMonthLength(m);
        }
        return precedingMonthDays + dayOfMonth;
    }

    private static int getMonthLength(int m) {
        return MONTH_LENGTHS[m - 1];
    }
}