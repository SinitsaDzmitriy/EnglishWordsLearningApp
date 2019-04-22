package com.name.common.util;

final public class REQUEST {

    public static final String SEPARATOR = " ";

    public class GET {
        public static final String METHOD = "get";

        final public class DATA_TYPE {
            public static final String BASE = "base";
            public static final String USER = "user";

            private DATA_TYPE() { }
        }

        private GET() { }
    }

    public class VERIFY {
        public static final String METHOD = "verify";

        final public class DATA_TYPE {
            public static final String LOGIN = "login";
            public static final String PAIR = "pair";

            private DATA_TYPE() { }
        }

        private VERIFY() { }
    }

    private REQUEST() { }
}