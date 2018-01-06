package ph.biochem.modules;

public class DataHolder {
    public static String temperature="", pulseRate="", respiratoryRate="", bloodPressure="", eyeGlasses="", colorVision="",
            right="", left="", bmiRemarks="";
    public static double weight=0, height=0, BMI=0;

    public static String hospitalizations="", surgery="", presentMed="", remarks="", smoker="", alcohol="", mensDate="",
            gravida="", para="", t="", p="", a="", l="", m="", sticksPerDay="", smokerYrs="", bottlesPerSession="", drinkerYrs="";

    //
    public static String fasting="", bloodUrea="", creatinine="", bloodUric="", totalCholesterol="", triglycerides="", HDL="",
            LDL="", SGPT="", SGOT="", bloodChemistryRemarks="";

    public static String wbc="", lymphocyte="", monocyte="", granulocytes="", MCV="", MCH="", MCHC="", CBCRBC="", Hemoglobin="",
            Hermatocrit="", Platelet="", cbcRemarks="", testType="";

    public static String color="", consistency="", grossOther="",
            occultBlood="", pusCells="", FARBC="", microscopicOther="", FARemarks="";

    public static String UAColor="", transparency="", protein="", sugar="", specgrav="", phLevel="", UAPus="", UARBC="",
            epithelial="", mucus="", bacteria="", urates="", phosphates="", casts="", crystals="", UAOthers="", UARemarks="";

    public static String miscTests, miscRemarks;

    public static boolean familyFormOpened = false, clinicalFormOpened = false;

    public static int selectedMRNID = 0;

    public static ConfigManagement config;
}
