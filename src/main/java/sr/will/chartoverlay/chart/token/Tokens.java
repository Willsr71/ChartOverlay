package sr.will.chartoverlay.chart.token;

import java.util.*;

import static sr.will.chartoverlay.chart.token.Tokens.TType.MULTI_STRING;
import static sr.will.chartoverlay.chart.token.Tokens.TType.SINGLE_REGEX;

public enum Tokens {
    // Common tokens
    VERSION(new StringToken("VER", "version")),
    COPYRIGHT(new StringToken("CRR", "copyright")),
    CHART_EDITION(new MapToken("CED", "chartEdition", Arrays.asList(
            new IntegerToken("SE", "sourceEdition"),
            new IntegerToken("RE", "rasterEdition"),
            new StringToken("ED", "editionDate")
    ))),
    NOTICE_TO_MARINERS(new MapToken("NTM", "noticeToMariners", Arrays.asList(
            new DoubleToken("NE", "editionNumber"),
            new StringToken("ND", "date")
    ))),

    // BSB tokens
    CHART_INFO(new MapToken("CHT", "chartInfo", Arrays.asList(
            new StringToken("NA", "name"),
            new StringToken("NU", "number")
    ))),
    CHART_FORMAT(new StringToken("CHF", "chartFormat")),
    COAST_GUARD_DISTRICT(new ListToken("CGD", "coastGuardDistrict", IntegerToken.BLANK)),
    REGION(new ListToken("RGN", "region", IntegerToken.BLANK)),
    CHART_KAP(new ListToken("CHK", "chartKapInfo", IntegerToken.BLANK)),
    ORGANISATION(new StringToken("ORG", "organisation")),
    MANUFACTURER(new StringToken("MFR", "manufacturer")),
    KAP_FILES(new MapToken("K[0-9]{2}", "kapFiles", Arrays.asList(
            new StringToken("NA", "name"),
            new IntegerToken("NU", "number"),
            new StringToken("TY", "type"),
            new StringToken("FN", "fileName")
    )), SINGLE_REGEX),

    // KAP tokens
    PANE_INFO(new MapToken("BSB", "paneInfo", Arrays.asList(
            new StringToken("NA", "name"),
            new IntegerToken("NU", "number"),
            new ListToken("RA", "dimensions", IntegerToken.BLANK),
            new IntegerToken("DU", "dpi")
    ))),
    PANE_PARAMS(new MapToken("KNP", "paneParameters", Arrays.asList(
            new IntegerToken("SC", "scale"),
            new StringToken("GD", "geodeticDatum"),
            new StringToken("PR", "projection"),
            new StringToken("PP", "projectionParameter"),
            new StringToken("PI", "projectionInterval"),
            new DoubleToken("SK", "skewAngle"),
            new DoubleToken("TA", "textAngle"),
            new StringToken("UN", "depthUnits"),
            new StringToken("SD", "soundingDatum"),
            new DoubleToken("DX", "pixelDistanceX"),
            new DoubleToken("DY", "pixelDistanceY")
    ))),
    ADDITIONAL_PANE_PARAMS(new MapToken("KNQ", "additionalPaneParameters")),
    OFFSET_STRIP_IMAGE_LINES(new IntegerToken("OST", "offsetStripImageLines")),
    COLORMAP_DEPTH(new IntegerToken("IFM", "colormapDepth")),
    PHASE_SHIFT(new DoubleToken("CPH", "phaseShift")),
    POLY_LONG_TO_X(new ListToken("WPX", "polyLongToX", Arrays.asList(IntegerToken.BLANK, DoubleToken.BLANK))),
    POLY_LAT_TO_Y(new ListToken("WPY", "polyLatToY", Arrays.asList(IntegerToken.BLANK, DoubleToken.BLANK))),
    POLY_X_TO_LONG(new ListToken("PWX", "polyXtoLong", Arrays.asList(IntegerToken.BLANK, DoubleToken.BLANK))),
    POLY_Y_TO_LAT(new ListToken("PWY", "polyYtoLat", Arrays.asList(IntegerToken.BLANK, DoubleToken.BLANK))),
    DATUM_SHIFT(new ListToken("DTM", "datumShift", DoubleToken.BLANK)),
    ORG_UPDATE_DATES(new ListToken("ADN[0-9]{4}", "orgUpdateDates").setSplitToken(";"), SINGLE_REGEX),
    CHART_UPDATES(new ListToken("ARE[0-9]{4}", "chartUpdates", Arrays.asList(
            null, DoubleToken.BLANK, DoubleToken.BLANK, StringToken.BLANK, StringToken.BLANK, StringToken.BLANK, ListToken.BLANK_PIPE
    )), SINGLE_REGEX),
    COLOR_RGB(new ListToken("RGB", "colorRGB", IntegerToken.BLANK), MULTI_STRING),
    COLOR_GRAY(new ListToken("GRY", "colorGray", IntegerToken.BLANK), MULTI_STRING),
    COLOR_DAY(new ListToken("DAY", "colorDay", IntegerToken.BLANK), MULTI_STRING),
    COLOR_DUSK(new ListToken("DSK", "colorDusk", IntegerToken.BLANK), MULTI_STRING),
    COLOR_NIGHT(new ListToken("NGT", "colorNight", IntegerToken.BLANK), MULTI_STRING),
    COLOR_NIGHT_RED(new ListToken("NGR", "colorNightRed", IntegerToken.BLANK), MULTI_STRING),
    COLOR_PRC(new ListToken("PRC", "colorPRC", IntegerToken.BLANK), MULTI_STRING),
    COLOR_PRG(new ListToken("PRG", "colorPRG", IntegerToken.BLANK), MULTI_STRING),
    REFERENCE_POINTS(new ListToken("REF", "referencePoints", Arrays.asList(
            IntegerToken.BLANK, IntegerToken.BLANK, IntegerToken.BLANK, DoubleToken.BLANK
    )), MULTI_STRING),
    REFERENCE_POINT_ERROR(new ListToken("ERR", "referencePointError", Arrays.asList(
            IntegerToken.BLANK, DoubleToken.BLANK
    )), MULTI_STRING),
    POLYGON(new ListToken("PLY", "polygon", Arrays.asList(
            IntegerToken.BLANK, DoubleToken.BLANK
    )), MULTI_STRING);

    private static boolean populated = false;
    private static final Map<TType, List<Tokens>> tokens = new HashMap<>();

    private final Token token;
    private final TType type;

    Tokens(Token token, TType type) {
        this.token = token;
        this.type = type;
    }

    Tokens(Token token) {
        this.token = token;
        this.type = TType.SINGLE_STRING;
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

    public Token subToken(String name) {
        return token.getSubToken(name);
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
        for (TType type : TType.values()) {
            tokens.put(type, new ArrayList<>());
        }

        for (Tokens t : values()) {
            tokens.get(t.type).add(t);
        }
        populated = true;
    }

    public static List<Tokens> getByType(TType type) {
        if (!populated) populateLists();
        return tokens.get(type);
    }

    public enum TType {
        SINGLE_STRING,
        SINGLE_REGEX,
        MULTI_STRING,
        MULTI_REGEX
    }
}
