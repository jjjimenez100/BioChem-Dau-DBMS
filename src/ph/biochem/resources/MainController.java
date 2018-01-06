package ph.biochem.resources;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ph.biochem.models.Patient;
import ph.biochem.modules.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class MainController extends AlertDialog{
    @FXML
    private Button btnAddPatient,  btnEditPatient, btnDeletePatient, btnCancel;
    @FXML
    private Button btnAddPatientSave;
    @FXML
    private JFXToggleButton toggleSearch;
    @FXML
    private JFXTextField inputSearch;
    @FXML
    private JFXComboBox comboCategory;
    @FXML
    private TableView<Patient> tblPatients;
    @FXML
    private TableColumn colMRN, colName, colAddress, colGender, colCompanyName, colContactNumber, colDate, colOccupation;

    /**
     * INPUT FIELDS PERSONAL
     */
    @FXML
    private JFXTextField inputName, inputAddress, inputContactNumber, inputCompanyName, inputOccupation;

    @FXML
    private Label lblPatient, lblDate, lblBirthday, lblEmployment, lblRequired;

    @FXML
    private JFXComboBox comboCivilStatus, comboDateMonth, comboDateDay, comboDateYear, comboBirthdayMonth, comboBirthdayDay, comboBirthdayYear, comboEmploymentStatus, comboPurpose;

    @FXML
    private JFXRadioButton radioGenderMale, radioGenderFemale;

    /**
     * END INPUT FIELDS PERSONAL
     */

    /**
     * INPUT FIELDS SECONDARY PERSONAL
     */
    @FXML
    private Label lblMedical, lblLaboratory, lblRadioGraphic;

    @FXML
    private Button btnClinicalMeasurements, btnFamilyHealthHistory;

    @FXML
    private JFXRadioButton radioPackages, radioIndividualTests, radioCorporate, radioSanitary;

    @FXML
    private JFXComboBox comboTestType;

    @FXML
    private TextArea txtAreaChestPA, txtAreaImpression;

    @FXML
    private JFXTextField inputOtherTests;
    /**
     * END INPUT FIELDS SECONDARY PERSONAL
     */

    private ObservableList<Patient> patients;
    private boolean corporate, sanitary, individual;
    private boolean radioGraphic, CBC, UA, FA, MISC, bloodChemistry;
    private int userMode;

    /**
     * Controllers
     */
    private ClinicalController clinicalController;
    private FamilyHealthController familyHealthController;
    private BloodChemistryController bloodChemistryController;
    private CBCController cbcController;
    private FAController faController;
    private MISCController miscController;
    private UAController uaController;
    /**
     * END CONTROLLERS
     */

    @FXML
    public void initialize(){
        DBHelper.connectToDatabase();
        initTableView();
        initComboBoxes();
    }

    private void initTableView(){
        colMRN = new TableColumn("MRN");
        colName = new TableColumn("Name");
        colAddress = new TableColumn("Address");
        colGender = new TableColumn("Gender");
        colCompanyName = new TableColumn("Company");
        colContactNumber = new TableColumn("Contact #");
        colDate = new TableColumn("Date");
        colOccupation = new TableColumn("Occupation");

        colMRN.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("MRN"));
        colName.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Patient, String>("homeAddress"));
        colGender.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<Patient, String>("companyName"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<Patient, String>("contactNumber"));
        colDate.setCellValueFactory(new PropertyValueFactory<Patient, String>("date"));
        colOccupation.setCellValueFactory(new PropertyValueFactory<Patient, String>("occupation"));

        DBHelper.executeQuery("SELECT * FROM PATIENTS");
        patients = FXCollections.observableArrayList();

        while(DBHelper.hasNextData()){
            patients.add(new Patient(DBHelper.getIntData("MRN"), DBHelper.getStrData("Name"), DBHelper.getStrData("HomeAddress")
                    , DBHelper.getStrData("ContactNumber"), DBHelper.getStrData("Gender"), DBHelper.getStrData("CompanyName"),
                    DBHelper.getStrData("Date"), DBHelper.getStrData("occupation")));
        }

        tblPatients.setItems(patients);
        tblPatients.getColumns().addAll(colMRN, colName, colAddress, colContactNumber, colGender, colCompanyName, colDate, colOccupation);
    }

    public void initComboBoxes(){
        initComboBox(comboBirthdayDay, comboDateDay, 1, 31, 1);
        initComboBox(comboBirthdayMonth, comboDateMonth, 1, 12, 1);
        initComboBox(comboBirthdayYear, comboDateYear, 1950, LocalDate.now().getYear(), 1);
        comboCivilStatus.getItems().addAll(new String[]{"Single", "Married", "Common Law", "Widow/er", "Legally Seperated"});
        comboEmploymentStatus.getItems().addAll(new String[]{"New Applicant", "Probationary", "Regular", "Contractual"});
        comboPurpose.getItems().addAll((new String[]{"Medical Consulation", "PEME", "Annual Medical Examination"}));
        comboTestType.getItems().addAll(new String[]{"Radiographic", "CBC", "UA", "FA", "Blood Chemistry", "Misc"});
    }

    public void initComboBox(JFXComboBox combo, JFXComboBox combo2, int start, int end, int increment){
        if(combo2 == null){
            for( ; start<=end ; start++){
                combo.getItems().add(start);
            }
        }
        else{
            for( ; start<=end ; start++){
                combo.getItems().add(start);
                combo2.getItems().add(start);
            }
        }
    }

    public void addPatient(){
        disableAddInputFields(false);
        disableControlButtons(true);
        disableCancelButton(false);
        btnAddPatientSave.setDisable(false);
        userMode = 1;
        if(DataHolder.clinicalFormOpened){
            clinicalController.resetValues();
        }
        if(DataHolder.familyFormOpened){
            familyHealthController.resetValues();
        }
        clearInputFields();
    }

    public void onClickBtnSave() throws Exception{
        String directoryPath = "FindAndReplace/FindAndReplace/bin/debug/config/";
        if(userMode == 1){ //add user
            insertNewPatient();
            tblPatients.requestFocus();
            tblPatients.getSelectionModel().select(patients.size()-1);
            tblPatients.getFocusModel().focus(patients.size()-1);
            tblPatients.scrollTo(patients.size()-1);
            disableAddInputFields(true);
            disableControlButtons(false);
            btnAddPatientSave.setDisable(true);
            disableCancelButton(true);
        }
        else{ //update user
            String updatePatientsQuery = "UPDATE PATIENTS SET NAME = ?, HomeAddress = ?, ContactNumber = ?, Gender = ?, " +
                    "CompanyName = ?, Date = ?, Occupation = ? WHERE MRN = ?";
            String name = inputName.getText();
            String address = inputAddress.getText();
            String contactNumber = inputContactNumber.getText();
            String gender = radioGenderFemale.isSelected() ? radioGenderFemale.getText() : radioGenderMale.getText();
            String companyName = inputCompanyName.getText();
            String date = comboDateMonth.getValue().toString() + "/" + comboDateDay.getValue().toString() + "/" + comboDateYear.getValue().toString();
            String occupation = inputOccupation.getText();

            DBHelper.executeQuery(updatePatientsQuery, new String[]{name, address, contactNumber, gender,
                    companyName, date, occupation, Integer.toString(DataHolder.selectedMRNID)}, StatementType.UPDATE);
            Patient updatedPatient = patients.get(findPatientIndex(DataHolder.selectedMRNID));
            updatedPatient.setName(name);
            updatedPatient.setHomeAddress(address);
            updatedPatient.setCompanyName(companyName);
            updatedPatient.setContactNumber(contactNumber);
            updatedPatient.setGender(gender);
            updatedPatient.setDate(date);
            updatedPatient.setOccupation(occupation);
            tblPatients.refresh();

            String updateSecondaryInfo = "UPDATE SecondaryInfo SET CivilStatus = ?, Birthday = ?, EmploymentStatus = ?, Purpose = ? WHERE MRNID = ?";
            String birthday = isNull(comboBirthdayMonth.getValue())+"/"+isNull(comboBirthdayDay.getValue())+"/"+isNull(comboBirthdayYear.getValue());
            DBHelper.executeQuery(updateSecondaryInfo, new String[]{isNull(comboCivilStatus.getValue()), birthday,
                    isNull(comboEmploymentStatus.getValue()), isNull(comboPurpose.getValue()), Integer.toString(DataHolder.selectedMRNID)}, StatementType.UPDATE);

            if(DataHolder.familyFormOpened){
                updateSecondaryInfo = "UPDATE SecondaryInfo SET Hospitalizations = ?, Surgery = ?, " +
                        "PresentMedications = ?, FamilyHistoryRemarks = ?, Smoker = ?, SticksPerDay = ?, SmokerYrs = ?, AlcoholDrinker = ?, " +
                        "BottlesPerSession = ?, DrinkerYrs = ?, Menstruation = ?, Gravida = ?, Para = ?, TFemale = ?, PFemale = ?, AFemale = ?, " +
                        "LFemale = ?, MFemale = ? WHERE MRNID = ?";
                DBHelper.executeQuery(updateSecondaryInfo, new String[]{isNull(familyHealthController.hospitalizations), isNull(familyHealthController.surgery),
                        isNull(familyHealthController.presentMed), familyHealthController.remarks, isNull(familyHealthController.smoker), familyHealthController.sticksPerDay,
                        familyHealthController.smokerYrs, isNull(familyHealthController.alcohol), familyHealthController.bottlesPerSession, familyHealthController.drinkerYrs,
                        isNull(familyHealthController.mensDate), familyHealthController.gravida, familyHealthController.para, familyHealthController.t,
                        familyHealthController.p, familyHealthController.a, familyHealthController.l, familyHealthController.m,
                        Integer.toString(DataHolder.selectedMRNID)}, StatementType.UPDATE);
            }
            if(DataHolder.clinicalFormOpened){
                updateSecondaryInfo = "UPDATE SecondaryInfo SET Temperature = ?, PulseRate = ?, RespiratoryRate = ?, BloodPressure = ?, Weight = ?, Height = ?, Score = ?, " +
                        "BMIRemarks = ?, EyeGlasses = ?, ColorVision = ?, RightEye = ?, LeftEye = ? WHERE MRNID = ?";
                DBHelper.executeQuery(updateSecondaryInfo, new String[]{clinicalController.temperature, clinicalController.pulseRate,
                        clinicalController.respiratoryRate, clinicalController.bloodPressure, Double.toString(clinicalController.weight), Double.toString(clinicalController.height),
                        Double.toString(clinicalController.BMI), clinicalController.bmiRemarks, isNull(clinicalController.eyeGlasses), isNull(clinicalController.colorVision),
                        clinicalController.right, clinicalController.left, Integer.toString(DataHolder.selectedMRNID)}, StatementType.UPDATE);
            }

            //TODO: TESTS
            //CBC, UA, FA, MISC, bloodChemistry
            if(corporate){
                DataHolder.config = new ConfigManagement(true, false, false, directoryPath, name, companyName, inputOtherTests.getText());
            }
            else if(sanitary){
                DataHolder.config = new ConfigManagement(false, true, false, directoryPath, name, companyName, inputOtherTests.getText());
            }
            else{
                DataHolder.config = new ConfigManagement(false, false, true, directoryPath, name, companyName, inputOtherTests.getText());
            }

            if(CBC){
                cbcController = showNewScene("CBC.fxml").<CBCController>getController();
            }
            if(UA){
                uaController = showNewScene("UA.fxml").<UAController>getController();
            }
            if(FA){
                faController = showNewScene("FA.fxml").<FAController>getController();
            }
            if(MISC){
                miscController = showNewScene("Misc.fxml").<MISCController>getController();
            }
            if(bloodChemistry){
                bloodChemistryController = showNewScene("BloodChemistry.fxml").<BloodChemistryController>getController();
            }
            if(radioGraphic){
                String updateRadiographic = "UPDATE SecondaryInfo SET ChestPA = ?, Impression = ? WHERE MRNID = ?";
                DBHelper.executeQuery(updateRadiographic, new String[]{txtAreaChestPA.getText(), txtAreaImpression.getText(), Integer.toString(DataHolder.selectedMRNID)}, StatementType.UPDATE);
                DataHolder.config.createConfigTest("Radiographic.biochem", new String[]{"START",txtAreaChestPA.getText(),"END", txtAreaImpression.getText()});
            }
            DBHelper.executeQuery("SELECT * FROM PATIENTS WHERE MRN = ?",
                    new String[]{Integer.toString(DataHolder.selectedMRNID)}, StatementType.SELECT);
            DataHolder.config.createConfigTest("PersonalInfo.biochem", new String[]{Integer.toString(DataHolder.selectedMRNID),
            name, getAge(birthday), gender, birthday, companyName});

            cancel();

            /*String updateCBC = "UPDATE SecondaryInfo SET CBCWBC = ?, CBCLymphocyte = ?, CBCMonocyte = ?, CBCGranulocyte = ?, " +
                    "CBCMCV = ?, CBCMCH = ?, CBCRBC = ?, CBCHemoglobin = ?, CBCHermatocrit = ?, CBCPlatelet = ?, CBCRemarks = ?," +
                    "CBCTestType = ? WHERE MRNID = ?";*/
            /*String updateUA = "UPDATE SecondaryInfo SET UAColor = ?, UATransparency = ?, UAProtein = ?, UASugar = ?, UASpecGrav = ?, " +
                    "UApHLevel = ?, UAPusCells = ?, UARBC = ?, UAEpithelial = ?, UAMucus = ?, UABacteria = ?, UAUrates = ?, " +
                    "UAPhosphates = ?, UACasts = ?, UACrystals = ?, UAOthers = ?, UARemarks = ? WHERE MRNID = ?";*/
            /*String updateFA = "UPDATE SecondaryInfo SET FAColor = ?, FAConsistency = ?, FAGrossOtherFindings = ?, FAOccultBlood = ?," +
                    " FAPusCells = ?, FARBC = ?, FAMicroscopicOtherFindings = ?, FARemarks = ? WHERE MRNID = ?";*/
            /*String updateBC = "UPDATE SecondaryInfo SET BCFastingBloodSugar = ?, BCBlood = ?, BCCreatinine = ?, BCUricAcid = ?, " +
                    "BCCholesterol = ?, BCTriglycerides = ?, BCHDL = ?, BCLDL = ?, BCSGPT = ?, BCSGOT = ?, BCRemarks = ? WHERE" +
                    " MRNID = ?";*/
            /*String updateMisc = "UPDATE SecondaryInfo SET MiscTests = ?, MiscRemarks = ? WHERE MRNID = ?";*/
        }
    }

    private String getAge(String birthday){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate convertedBirthday = LocalDate.parse(birthday, format);
        return Integer.toString(Period.between(convertedBirthday, LocalDate.now()).getYears());
    }

    private int findPatientIndex(int MRN){
        for(int index=0; index<patients.size(); index++){
            int patientMRN = patients.get(index).getMRN();
            if(patientMRN == MRN){
                return index;
            }
        }
        return -1;
    }

    private void insertNewPatient(){
       insertPatientPrimaryData();
       insertPatientSecondaryData();
    }

    public void insertPatientPrimaryData(){
        String name = inputName.getText();
        String address = inputAddress.getText();
        String contactNumber = inputContactNumber.getText();
        String gender = radioGenderFemale.isSelected() ? radioGenderFemale.getText() : radioGenderMale.getText();
        String companyName = inputCompanyName.getText();
        String date = comboDateMonth.getValue().toString() + "/" + comboDateDay.getValue().toString() + "/" + comboDateYear.getValue().toString();
        String occupation = inputOccupation.getText();

        String insertQuery = "INSERT INTO PATIENTS(Name, HomeAddress, ContactNumber, Gender, CompanyName, Date, Occupation) VALUES(?,?,?,?,?,?,?)";
        DBHelper.executeQuery(insertQuery, new String[]{name, address, contactNumber, gender, companyName, date, occupation}, StatementType.INSERT);
        patients.add(new Patient(DBHelper.getLastInsertedID(), name, address, contactNumber, gender, companyName, date, occupation));
    }

    public void insertPatientSecondaryData(){
        String insertQuery = "INSERT INTO SecondaryInfo(CivilStatus, Birthday, EmploymentStatus, Purpose, MRNID) VALUES(?,?,?,?,?)";
        String birthday = isNull(comboBirthdayMonth.getValue())+"/"+isNull(comboBirthdayDay.getValue())+"/"+isNull(comboBirthdayYear.getValue());
        DBHelper.executeQuery(insertQuery, new String[]{isNull(comboCivilStatus.getValue()), birthday,
                isNull(comboEmploymentStatus.getValue()), isNull(comboPurpose.getValue()),
                Integer.toString(DBHelper.getLastInsertedID())}, StatementType.INSERT);

        if(DataHolder.clinicalFormOpened){
            String updateQuery = "UPDATE SecondaryInfo SET Temperature = ?, PulseRate = ?, RespiratoryRate = ?, BloodPressure = ?, " +
                    "Weight = ?, Height = ?, Score = ?, BMIRemarks = ?, EyeGlasses = ?, ColorVision = ?, RightEye = ?, " +
                    "LeftEye = ? WHERE MRNID = ?";
           /* insertQuery = "INSERT INTO SecondaryInfo(Temperature, PulseRate, RespiratoryRate, BloodPressure, Weight, Height, Score, BMIRemarks, EyeGlasses, ColorVision, RightEye," +
                    "LeftEye, MRNID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";*/
            DBHelper.executeQuery(updateQuery, new String[]{isNull(clinicalController.temperature), clinicalController.pulseRate,
                    clinicalController.respiratoryRate, clinicalController.bloodPressure, Double.toString(clinicalController.weight), Double.toString(clinicalController.height),
                    Double.toString(clinicalController.BMI), clinicalController.bmiRemarks, isNull(clinicalController.eyeGlasses), isNull(clinicalController.colorVision),
                    clinicalController.right, clinicalController.left, Integer.toString(DBHelper.getLastInsertedID())}, StatementType.INSERT);
        }
        if(DataHolder.familyFormOpened){
            String updateQuery = "UPDATE SecondaryInfo SET Hospitalizations = ?, Surgery = ?, PresentMedications = ?, FamilyHistoryRemarks = ?, " +
                    "Smoker = ?, SticksPerDay = ?, SmokerYrs = ?, AlcoholDrinker = ?, BottlesPerSession = ?, DrinkerYrs = ?, Menstruation = ?, Gravida = ?, " +
                    "Para = ?, TFemale = ?, PFemale = ?, AFemale = ?, LFemale = ?, MFemale = ? WHERE MRNID = ?";
            /*insertQuery = "INSERT INTO SecondaryInfo(Hospitalizations, Surgery, PresentMedications, FamilyHistoryRemarks, Smoker, SticksPerDay, SmokerYrs, " +
                    "AlcoholDrinker, BottlesPerSession, DrinkerYrs, Menstruation, Gravida, Para, TFemale, PFemale, AFemale, LFemale," +
                    "MFemale, MRNID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";*/
            DBHelper.executeQuery(updateQuery, new String[]{isNull(familyHealthController.hospitalizations), isNull(familyHealthController.surgery),
                    isNull(familyHealthController.presentMed), familyHealthController.remarks, isNull(familyHealthController.smoker), familyHealthController.sticksPerDay,
                    familyHealthController.smokerYrs, isNull(familyHealthController.alcohol), familyHealthController.bottlesPerSession, familyHealthController.drinkerYrs,
                    isNull(familyHealthController.mensDate), familyHealthController.gravida, familyHealthController.para, familyHealthController.t,
                    familyHealthController.p, familyHealthController.a, familyHealthController.l, familyHealthController.m,
                    Integer.toString(DBHelper.getLastInsertedID())}, StatementType.INSERT);
        }
    }

    public void editPatient(){
        Patient selectedPatient = tblPatients.getSelectionModel().getSelectedItem();
        if(selectedPatient != null){
            disableControlButtons(true);
            disableCancelButton(false);
            btnAddPatientSave.setDisable(false);
            disableAddInputFields(false);
            tblPatients.setDisable(true);
            disableLaboratoryResults(false);
            comboTestType.setDisable(true);
            userMode = 2;

            DataHolder.selectedMRNID = selectedPatient.getMRN();
            DBHelper.executeQuery("SELECT * FROM SecondaryInfo WHERE MRNID = " + DataHolder.selectedMRNID);

            inputName.setText(selectedPatient.getName());
            inputAddress.setText(selectedPatient.getHomeAddress());
            inputContactNumber.setText(selectedPatient.getContactNumber());

            if(selectedPatient.getGender().equals("Male")){
                radioGenderMale.setSelected(true);
            }
            else{
                radioGenderFemale.setSelected(true);
            }

            inputCompanyName.setText(selectedPatient.getCompanyName());
            String[] dates = selectedPatient.getDate().split("/");
            comboDateMonth.setValue(Integer.parseInt(dates[0]));
            comboDateDay.setValue(Integer.parseInt(dates[1]));
            comboDateYear.setValue(Integer.parseInt(dates[2]));

            inputOccupation.setText(selectedPatient.getOccupation());

            String birthday = DBHelper.getStrData("Birthday");
            String[] birthSplit = birthday.split("/");
            comboBirthdayMonth.setValue(Integer.parseInt(birthSplit[0]));
            comboBirthdayDay.setValue(Integer.parseInt(birthSplit[1]));
            comboBirthdayYear.setValue(Integer.parseInt(birthSplit[2]));

            comboEmploymentStatus.setValue(DBHelper.getStrData("EmploymentStatus"));
            comboPurpose.setValue(DBHelper.getStrData("Purpose"));
            comboCivilStatus.setValue(DBHelper.getStrData("CivilStatus"));

            //cache values
            DataHolder.temperature = DBHelper.getStrData("Temperature");
            DataHolder.pulseRate = DBHelper.getStrData("PulseRate");
            DataHolder.respiratoryRate = DBHelper.getStrData("RespiratoryRate");
            DataHolder.bloodPressure = DBHelper.getStrData("BloodPressure");
            DataHolder.weight = DBHelper.getIntData("Weight");
            DataHolder.height = DBHelper.getIntData("Height");
            DataHolder.BMI = DBHelper.getIntData("Score");
            DataHolder.bmiRemarks = DBHelper.getStrData("BMIRemarks");
            DataHolder.eyeGlasses = DBHelper.getStrData("EyeGlasses");
            DataHolder.colorVision = DBHelper.getStrData("ColorVision");
            DataHolder.right = DBHelper.getStrData("RightEye");
            DataHolder.left = DBHelper.getStrData("LeftEye");
            DataHolder.hospitalizations = DBHelper.getStrData("Hospitalizations");
            DataHolder.surgery = DBHelper.getStrData("Surgery");
            DataHolder.presentMed = DBHelper.getStrData("PresentMedications");
            DataHolder.remarks = DBHelper.getStrData("FamilyHistoryRemarks");
            DataHolder.smoker = DBHelper.getStrData("Smoker");
            DataHolder.sticksPerDay = DBHelper.getStrData("SticksPerDay");
            DataHolder.smokerYrs = DBHelper.getStrData("SmokerYrs");
            DataHolder.alcohol = DBHelper.getStrData("AlcoholDrinker");
            DataHolder.bottlesPerSession = DBHelper.getStrData("BottlesPerSession");
            DataHolder.drinkerYrs = DBHelper.getStrData("DrinkerYrs");
            DataHolder.mensDate = DBHelper.getStrData("Menstruation");
            DataHolder.gravida = DBHelper.getStrData("Gravida");
            DataHolder.para = DBHelper.getStrData("Para");
            DataHolder.t = DBHelper.getStrData("TFemale");
            DataHolder.p = DBHelper.getStrData("PFemale");
            DataHolder.a = DBHelper.getStrData("AFemale");
            DataHolder.l = DBHelper.getStrData("LFemale");
            DataHolder.m = DBHelper.getStrData("MFemale");
        }
    }

    public String isNull(Object o){
        if(o == null){
            return "";
        }
        else{
            return o.toString();
        }
    }

    public void cancel(){
        disableAddInputFields(true);
        disableControlButtons(false);
        disableCancelButton(true);
        btnAddPatientSave.setDisable(true);
        tblPatients.setDisable(false);
        disableLaboratoryResults(true);
        disableRadioGraphic(true);
    }

    public void disableAddInputFields(boolean value){
        inputName.setDisable(value);
        inputAddress.setDisable(value);
        inputContactNumber.setDisable(value);
        comboCivilStatus.setDisable(value);
        comboDateDay.setDisable(value);
        comboDateMonth.setDisable(value);
        comboDateYear.setDisable(value);
        comboBirthdayDay.setDisable(value);
        comboBirthdayMonth.setDisable(value);
        comboBirthdayYear.setDisable(value);
        radioGenderFemale.setDisable(value);
        radioGenderMale.setDisable(value);
        inputCompanyName.setDisable(value);
        inputOccupation.setDisable(value);
        comboEmploymentStatus.setDisable(value);
        comboPurpose.setDisable(value);
        lblBirthday.setDisable(value);
        lblDate.setDisable(value);
        lblEmployment.setDisable(value);
        lblPatient.setDisable(value);
        lblMedical.setDisable(value);
        lblRequired.setDisable(value);
        btnClinicalMeasurements.setDisable(value);
        btnFamilyHealthHistory.setDisable(value);
        /*lblLaboratory.setDisable(value);
        radioPackages.setDisable(value);
        radioIndividualTests.setDisable(value);*/
    }

    public void disableControlButtons(boolean value){
        btnAddPatient.setDisable(value);
        btnEditPatient.setDisable(value);
        btnDeletePatient.setDisable(value);
    }

    public void disableCancelButton(boolean value){
        btnCancel.setDisable(value);
    }

    public void onClickBtnClinicalMeasurements() throws Exception{
        DataHolder.clinicalFormOpened = true;
        clinicalController = showNewScene("ClinicalMeasurements.fxml").<ClinicalController>getController();
    }

    public void onClickBtnFamilyHistory() throws Exception {
        DataHolder.familyFormOpened = true;
        familyHealthController = showNewScene("FamilyHealthHistory.fxml").<FamilyHealthController>getController();
    }

    public FXMLLoader showNewScene(String relativePath) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(relativePath));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        return loader;
    }

    public void packagesOnChange(){
        comboTestType.setDisable(true);
        radioCorporate.setDisable(false);
        radioSanitary.setDisable(false);
        inputOtherTests.setDisable(false);
        disableRadioGraphic(true);
    }

    public void individualTestsOnChange(){
        comboTestType.setDisable(false);
        radioCorporate.setDisable(true);
        radioSanitary.setDisable(true);
        inputOtherTests.setDisable(true);
        disableRadioGraphic(true);
        corporate = sanitary = false;
        individual = true;
    }

    public void onChangeCorporate(){
        comboTestType.setValue("");
        CBC = UA = FA = radioGraphic = true;
        bloodChemistry = false;
        MISC = false;
        corporate = true;
        sanitary = false;
        individual = false;
        disableRadioGraphic(false);
    }

    public void onChangeSanitary(){
        comboTestType.setValue("");
        UA = FA = radioGraphic = true;
        CBC = false;
        bloodChemistry = false;
        MISC = false;
        corporate = false;
        sanitary = true;
        individual = false;
        disableRadioGraphic(false);
    }

    public void onChangeComboOtherTests(){
        if(comboTestType.getValue() != null){
            String testChoice = comboTestType.getValue().toString();
            if(testChoice.equals("Radiographic")){
                radioGraphic = true;
                CBC = UA = FA = bloodChemistry = MISC = false;
                disableRadioGraphic(false);
            }
            else if(testChoice.equals("CBC")){
                CBC = true;
                radioGraphic = UA = FA = bloodChemistry = MISC = false;
                disableRadioGraphic(true);
            }
            else if(testChoice.equals("UA")){
                UA = true;
                radioGraphic = CBC = FA = bloodChemistry = MISC = false;
                disableRadioGraphic(true);
            }
            else if(testChoice.equals("FA")){
                FA = true;
                radioGraphic = CBC = UA = bloodChemistry = MISC = false;
                disableRadioGraphic(true);
            }
            else if(testChoice.equals("Blood Chemistry")){
                bloodChemistry = true;
                radioGraphic = CBC = UA = FA = MISC = false;
                disableRadioGraphic(true);
            }
            else if(testChoice.equals("Misc")){
                MISC = true;
                radioGraphic = CBC = UA = FA = bloodChemistry = false;
                disableRadioGraphic(true);
            }
        }
    }

    public void disableRadioGraphic(boolean value){
        lblRadioGraphic.setDisable(value);
        txtAreaChestPA.setDisable(value);
        txtAreaImpression.setDisable(value);
    }

    public void disableLaboratoryResults(boolean value){
        lblLaboratory.setDisable(value);
        radioPackages.setDisable(value);
        radioIndividualTests.setDisable(value);
        comboTestType.setDisable(value);
        radioCorporate.setDisable(value);
        radioSanitary.setDisable(value);
        inputOtherTests.setDisable(value);
    }

    public void clearInputFields(){
        inputName.setText("");
        inputAddress.setText("");
        inputContactNumber.setText("");
        comboCivilStatus.setValue("");
        comboDateYear.setValue("");
        comboDateDay.setValue("");
        comboDateMonth.setValue("");
        comboBirthdayDay.setValue("");
        comboBirthdayMonth.setValue("");
        comboBirthdayYear.setValue("");

    }

    public void onClickBtnDelete(){
        if(showDialogBox("Confirmation", "Are you sure you want to delete the selected patient?")){
            Patient selectedPatient = tblPatients.getSelectionModel().getSelectedItem();
            String deletePatient = "DELETE from PATIENTS WHERE MRN = ?";
            String deleteSecondaryInfo = "DELETE from SecondaryInfo where MRNID = ?";
            int patientMRN = selectedPatient.getMRN();
            DBHelper.executeQuery(deletePatient, new String[]{Integer.toString(patientMRN)}, StatementType.DELETE);
            DBHelper.executeQuery(deleteSecondaryInfo, new String[]{Integer.toString(patientMRN)}, StatementType.DELETE);
            patients.remove(selectedPatient);
            tblPatients.refresh();
        }
    }
}
