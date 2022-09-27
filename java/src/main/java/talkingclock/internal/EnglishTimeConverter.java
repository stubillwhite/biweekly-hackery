package talkingclock.internal;

import java.util.List;

public class EnglishTimeConverter {

    private static final List<String> TENS =
            List.of("oh", "ten", "twenty", "thirty", "forty", "fifty");

    private static final List<String> UNITS_AND_TEENS =
            List.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                    "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen");

    public String convertToEnglish(String timeStr) {
        final String hours = convertHours(timeStr);
        final String minutes = convertMinutes(timeStr);
        final String period = convertPeriod(timeStr);

        if (hasMinutes(timeStr)) {
            return String.format("It is %s %s %s", hours, minutes, period);
        } else {
            return String.format("It is %s %s", hours, period);
        }
    }

    private boolean hasMinutes(String timeStr) {
        return Integer.parseInt(timeStr.split(":")[1]) != 0;
    }

    private boolean hasUnitMinutes(String timeStr) {
        return Integer.parseInt(timeStr.split(":")[1]) % 10 != 0;
    }

    private String convertHours(String timeStr) {
        final String hoursStr = timeStr.split(":")[0];
        final int hoursInTwelveHourClock = Integer.parseInt(hoursStr) % 12;
        return hoursInTwelveHourClock == 0 ? UNITS_AND_TEENS.get(12) : UNITS_AND_TEENS.get(hoursInTwelveHourClock);
    }

    private String convertMinutes(String timeStr) {
        final String minutesStr = timeStr.split(":")[1];
        final int minutes = Integer.parseInt(minutesStr);

        if ((10 <= minutes) && (minutes <= 19)) {
            return UNITS_AND_TEENS.get(minutes);
        } else {
            final int tens = Integer.parseInt(minutesStr.substring(0, 1));
            final int units = Integer.parseInt(minutesStr.substring(1, 2));

            final StringBuilder builder = new StringBuilder();

            builder.append(TENS.get(tens));

            if (hasUnitMinutes(timeStr)) {
                builder.append(" ").append(UNITS_AND_TEENS.get(units));
            }

            return builder.toString();
        }
    }

    private String convertPeriod(String timeStr) {
        final String hoursStr = timeStr.split(":")[0];
        return (Integer.parseInt(hoursStr) < 12) ? "a.m." : "p.m.";
    }
}
