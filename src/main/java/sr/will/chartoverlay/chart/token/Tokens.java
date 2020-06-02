package sr.will.chartoverlay.chart.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Tokens {
    VERSION(new StringToken("VER", "version")),
    COPYRIGHT(new StringToken("CRR", "copyright")),
    CHART_INFO(new MapToken("CHT", "chartInfo", Arrays.asList(
            new StringToken("NA", "name"),
            new StringToken("NU", "number")
    ))),
    CHART_FORMAT(new StringToken("CHF", "chartFormat")),
    COAST_GUARD_DISTRICT(new ListToken("CGD", "coastGuardDistrict", IntegerToken.BLANK)),
    REGION(new ListToken("RGN", "region", IntegerToken.BLANK)),
    CHART_EDITION(new MapToken("CED", "chartEdition", Arrays.asList(
            new IntegerToken("SE", "sourceEdition"),
            new IntegerToken("RE", "rasterEdition"),
            new StringToken("ED", "editionDate")
    ))),
    NOTICE_TO_MARINERS(new MapToken("NTM", "noticeToMariners", Arrays.asList(
            new DoubleToken("NE", "editionNumber"),
            new StringToken("ND", "date")
    ))),
    CHART_KAP(new ListToken("CHK", "chartKapInfo", IntegerToken.BLANK)),
    ORGANISATION(new StringToken("ORG", "organisation")),
    MANUFACTURER(new StringToken("MFR", "manufacturer")),
    KAP_FILES(new MapToken("K[0-9]{2}", "kapFiles", Arrays.asList(
            new StringToken("NA", "name"),
            new IntegerToken("NU", "number"),
            new StringToken("TY", "type"),
            new StringToken("FN", "fileName")
    )), true);

    private static boolean populated = false;
    private static final List<Tokens> tokens = new ArrayList<>();
    private static final List<Tokens> regexTokens = new ArrayList<>();

    private final Token token;
    private final boolean regex;

    Tokens(Token token, boolean regex) {
        this.token = token;
        this.regex = regex;
    }

    Tokens(Token token) {
        this.token = token;
        this.regex = false;
    }

    public String id() {
        return token.getId();
    }

    public Token getToken() {
        return token;
    }

    public String toString() {
        return token.getName();
    }

    public static Tokens getById(String id) {
        for (Tokens t : values()) {
            if (id.matches(t.id())) return t;
        }
        return null;
    }

    public static boolean isList(String id) {
        Tokens t = getById(id);
        return t != null && t.getToken() instanceof ListToken;
    }

    public static boolean isMap(String id) {
        Tokens t = getById(id);
        return t != null && t.getToken() instanceof MapToken;
    }

    private static void populateLists() {
        if (populated) return;
        for (Tokens t : values()) {
            if (t.regex) regexTokens.add(t);
            else tokens.add(t);
        }
    }

    public static List<Tokens> tokens() {
        populateLists();
        return tokens;
    }

    public static List<Tokens> regexTokens() {
        populateLists();
        return regexTokens;
    }
}
