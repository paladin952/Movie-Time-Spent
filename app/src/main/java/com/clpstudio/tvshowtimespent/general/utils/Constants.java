package com.clpstudio.tvshowtimespent.general.utils;

public class Constants {

    public class Time {
        /**
         * one second in milliseconds
         */
        public static final int ONE_SECOND = 1000;

        /**
         * two seconds in milliseconds
         */
        public static final int TWO_SECONDS = 2000;

        /**
         * three seconds in milliseconds
         */
        public static final int THREE_SECONDS = 3000;

        /**
         * The time interval after we make a api call on retrieving data suggestions
         */
        public static final int DEBOUNCE_TIME_INTERVAL = ONE_SECOND/2;
    }

    public class General {

        /**
         * The minimum number of character to send and api request
         */
        public static final int AUTOCOMPLETE_THRESHOLD = 3;

        /**
         * The end of sublist for autocomplete
         */
        public static final int AUTOCOMPLETE_SUBLIST_END = 5;

    }

    /**
     * Class that stores code-s for intents
     */
    public class Code {

        /**
         * The intent request code for voice speech
         */
        public static final int REQ_CODE_SPEECH_INPUT = 1;
    }

    public class Intents {
        public static final String KEY_TV_SHOW = "KEY_TV_SHOW";
    }
}
