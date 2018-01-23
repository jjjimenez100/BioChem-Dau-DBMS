package ph.biochem.resources;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ph.biochem.models.Patient;
import ph.biochem.modules.*;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
    private JFXTextField inputName, inputAddress, inputContactNumber, inputOccupation;

    @FXML
    private Label lblPatient, lblDate, lblBirthday, lblEmployment, lblTime, lblDateView;

    @FXML
    private JFXComboBox inputCompanyName, comboCivilStatus, comboDateMonth, comboDateDay, comboDateYear, comboBirthdayMonth, comboBirthdayDay, comboBirthdayYear, comboEmploymentStatus, comboPurpose;

    @FXML
    private JFXRadioButton radioGenderMale, radioGenderFemale;

    /**
     * END INPUT FIELDS PERSONAL
     */

    /**
     * INPUT FIELDS SECONDARY PERSONAL
     */
    @FXML
    private Label lblMedical, lblLaboratory, lblRadioGraphic, lblCompanyName;

    @FXML
    private Button btnClinicalMeasurements, btnFamilyHealthHistory, btnResults, btnPrint, btnOpenFolder;

    @FXML
    private JFXRadioButton radioPackages, radioIndividualTests, radioCorporate, radioSanitary;

    @FXML
    private JFXComboBox comboTestType;

    @FXML
    private TextArea txtAreaChestPA, txtAreaImpression;

    @FXML
    private JFXTextField inputOtherTests, inputMRNNo;
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
        if(!DataHolder.isAdmin){
            btnAddPatient.setDisable(true);
            btnEditPatient.setDisable(true);
            btnDeletePatient.setDisable(true);
        }
        DBHelper.connectToDatabase();
        initTableView();
        initComboBoxes();
        initTimeView();
        initDateView();
    }

    private void initDateView(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd yyyy, EEEE");
        lblDateView.setText(LocalDate.now().format(format));
    }

    private void initTimeView(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Calendar cal = Calendar.getInstance();
            int second = cal.get(Calendar.SECOND);
            int minute = cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR);

            lblTime.setText(hour + ":" + (minute) + ":" + second + " " + (cal.get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM") );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
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
            patients.add(new Patient(DBHelper.getStrData("MRN"), DBHelper.getStrData("Name"), DBHelper.getStrData("HomeAddress")
                    , DBHelper.getStrData("ContactNumber"), DBHelper.getStrData("Gender"), DBHelper.getStrData("CompanyName"),
                    DBHelper.getStrData("Date"), DBHelper.getStrData("occupation")));
        }

        FilteredList<Patient> filter = new FilteredList<>(patients);
        inputSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (patient.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (patient.getMRN().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (patient.getHomeAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (patient.getContactNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (patient.getGender().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (patient.getCompanyName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (patient.getDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (patient.getOccupation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Patient> sortedPatients = new SortedList<>(filter);
        sortedPatients.comparatorProperty().bind(tblPatients.comparatorProperty());

        tblPatients.getColumns().addAll(colMRN, colName, colAddress, colContactNumber, colGender, colCompanyName, colDate, colOccupation);
        tblPatients.setItems(sortedPatients);
    }

    public void onClickBtnOpenFolder(){
        Patient selectedPatient = tblPatients.getSelectionModel().getSelectedItem();
        if(selectedPatient != null){
            try{
                Desktop.getDesktop().open(new File("files/" +
                        selectedPatient.getCompanyName() + "/"
                        + selectedPatient.getMRN() + " "
                        + selectedPatient.getName()));
            }
            catch(Exception e){
                e.printStackTrace();
                showSuccess("Oops!", "Directory does not exist. Please re-check the directory name or" +
                        " update the patient's lab results first.");
            }
        }
        else{
            showSuccess("Oops!", "Select a patient from the table first.");
        }
    }

    private String replaceWhiteSpaces(String str){
        return str.replaceAll(" ", "%20");
    }

    public void initComboBoxes(){
        initComboBox(comboBirthdayDay, comboDateDay, 1, 31, 1);
        initComboBox(comboBirthdayMonth, comboDateMonth, 1, 12, 1);
        initComboBox(comboBirthdayYear, comboDateYear, 1950, LocalDate.now().getYear(), 1);
        comboCivilStatus.getItems().addAll(new String[]{"Single", "Married", "Common Law", "Widow/er", "Legally Seperated"});
        comboEmploymentStatus.getItems().addAll(new String[]{"New Applicant", "Probationary", "Regular", "Contractual"});
        comboPurpose.getItems().addAll((new String[]{"Medical Consulation", "PEME", "Annual Medical Examination"}));
        comboTestType.getItems().addAll(new String[]{"Radiographic", "CBC", "UA", "FA", "Blood Chemistry", "Misc"});

        //company names
        File file = new File("files/");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        inputCompanyName.getItems().addAll(directories);
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
                    "CompanyName = ?, Date = ?, Occupation = ?, MRN = ? WHERE MRN = ?";
            String name = inputName.getText();
            String address = inputAddress.getText();
            String contactNumber = inputContactNumber.getText();
            String gender = radioGenderFemale.isSelected() ? radioGenderFemale.getText() : radioGenderMale.getText();
            String companyName = isNull(inputCompanyName.getValue());
            String date = comboDateMonth.getValue().toString() + "/" + comboDateDay.getValue().toString() + "/" + comboDateYear.getValue().toString();
            String occupation = inputOccupation.getText();
            String MRNNo = inputMRNNo.getText();

            DBHelper.executeQuery(updatePatientsQuery, new String[]{name, address, contactNumber, gender,
                    companyName, date, occupation, MRNNo, DataHolder.selectedMRNID}, StatementType.UPDATE);
            Patient updatedPatient = patients.get(findPatientIndex(DataHolder.selectedMRNID));
            updatedPatient.setName(name);
            updatedPatient.setHomeAddress(address);
            updatedPatient.setCompanyName(companyName);
            updatedPatient.setContactNumber(contactNumber);
            updatedPatient.setGender(gender);
            updatedPatient.setDate(date);
            updatedPatient.setOccupation(occupation);
            updatedPatient.setMRN(MRNNo);
            tblPatients.refresh();

            String updateSecondaryInfo = "UPDATE SecondaryInfo SET CivilStatus = ?, Birthday = ?, EmploymentStatus = ?, Purpose = ? WHERE MRNID = ?";
            String birthday = isNull(comboBirthdayMonth.getValue())+"/"+isNull(comboBirthdayDay.getValue())+"/"+isNull(comboBirthdayYear.getValue());
            DBHelper.executeQuery(updateSecondaryInfo, new String[]{isNull(comboCivilStatus.getValue()), birthday,
                    isNull(comboEmploymentStatus.getValue()), isNull(comboPurpose.getValue()), DataHolder.selectedMRNID}, StatementType.UPDATE);

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
                        DataHolder.selectedMRNID}, StatementType.UPDATE);
            }
            if(DataHolder.clinicalFormOpened){
                updateSecondaryInfo = "UPDATE SecondaryInfo SET Temperature = ?, PulseRate = ?, RespiratoryRate = ?, BloodPressure = ?, Weight = ?, Height = ?, Score = ?, " +
                        "BMIRemarks = ?, EyeGlasses = ?, ColorVision = ?, RightEye = ?, LeftEye = ? WHERE MRNID = ?";
                DBHelper.executeQuery(updateSecondaryInfo, new String[]{clinicalController.temperature, clinicalController.pulseRate,
                        clinicalController.respiratoryRate, clinicalController.bloodPressure, Double.toString(clinicalController.weight), Double.toString(clinicalController.height),
                        Double.toString(clinicalController.BMI), clinicalController.bmiRemarks, isNull(clinicalController.eyeGlasses), isNull(clinicalController.colorVision),
                        clinicalController.right, clinicalController.left, DataHolder.selectedMRNID}, StatementType.UPDATE);
            }

            //TODO: TESTS
            //CBC, UA, FA, MISC, bloodChemistry
            if(corporate){
                DataHolder.config = new ConfigManagement(true, false, false, directoryPath, name, companyName, "", inputOtherTests.getText());
            }
            else if(sanitary){
                DataHolder.config = new ConfigManagement(false, true, false, directoryPath, name, companyName, "",inputOtherTests.getText());
            }
            else{
                DataHolder.config = new ConfigManagement(false, false, true, directoryPath, name, companyName,
                        comboTestType.getValue().toString(), inputOtherTests.getText());
            }

            showNewScene("Dates.fxml");
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
                DBHelper.executeQuery(updateRadiographic, new String[]{txtAreaChestPA.getText(), txtAreaImpression.getText(), DataHolder.selectedMRNID}, StatementType.UPDATE);
                DataHolder.config.createConfigTest("Radiographic.biochem", new String[]{"START",txtAreaChestPA.getText(),"END", txtAreaImpression.getText()});
            }
            DBHelper.executeQuery("SELECT * FROM PATIENTS WHERE MRN = ?",
                    new String[]{DataHolder.selectedMRNID}, StatementType.SELECT);
            DataHolder.config.createConfigTest("PersonalInfo.biochem", new String[]{DataHolder.selectedMRNID,
            name, getAge(birthday), gender, birthday, companyName});
            DataHolder.config.createConfigTest("Gender.biochem", new String[]{gender});
            DataHolder.config.createConfigTest("MRNNo.biochem", new String[]{DataHolder.selectedMRNID});
            btnResults.setDisable(false);
            btnAddPatientSave.setDisable(true);
            showSuccess("Message", "Enter lab results and click the generate button.");
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

    public void onClickBtnResults() throws IOException{
        Runtime.getRuntime().exec("FindAndReplace/FindAndReplace/bin/Debug/FindAndReplace.exe", null, new File("FindAndReplace/FindAndReplace/bin/Debug/"));
        cancel();
        btnResults.setDisable(true);
        showSuccess("Message", "Reports generated.");
    }

    private String getAge(String birthday){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate convertedBirthday = LocalDate.parse(birthday, format);
        return Integer.toString(Period.between(convertedBirthday, LocalDate.now()).getYears());
    }

    private int findPatientIndex(String MRN){
        for(int index=0; index<patients.size(); index++){
            String patientMRN = patients.get(index).getMRN();
            if(patientMRN.equalsIgnoreCase(MRN)){
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
        String companyName = isNull(inputCompanyName.getValue());
        String date = comboDateMonth.getValue().toString() + "/" + comboDateDay.getValue().toString() + "/" + comboDateYear.getValue().toString();
        String occupation = inputOccupation.getText();
        String MRNNo = inputMRNNo.getText();

        String insertQuery = "INSERT INTO PATIENTS(MRN, Name, HomeAddress, ContactNumber, Gender, CompanyName, Date, Occupation) VALUES(?,?,?,?,?,?,?,?)";
        DBHelper.executeQuery(insertQuery, new String[]{MRNNo, name, address, contactNumber, gender, companyName, date, occupation}, StatementType.INSERT);
        patients.add(new Patient(MRNNo, name, address, contactNumber, gender, companyName, date, occupation));
    }

    public void insertPatientSecondaryData(){
        String MRNNo = inputMRNNo.getText();
        String insertQuery = "INSERT INTO SecondaryInfo(CivilStatus, Birthday, EmploymentStatus, Purpose, MRNID) VALUES(?,?,?,?,?)";
        String birthday = isNull(comboBirthdayMonth.getValue())+"/"+isNull(comboBirthdayDay.getValue())+"/"+isNull(comboBirthdayYear.getValue());
        DBHelper.executeQuery(insertQuery, new String[]{isNull(comboCivilStatus.getValue()), birthday,
                isNull(comboEmploymentStatus.getValue()), isNull(comboPurpose.getValue()),
                MRNNo}, StatementType.INSERT);

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
            btnAddPatientSave.setDisable(true);
            userMode = 2;

            DataHolder.selectedMRNID = selectedPatient.getMRN();
            DBHelper.executeQuery("SELECT * FROM SecondaryInfo WHERE MRNID = '" + DataHolder.selectedMRNID + "'");
            inputName.setText(selectedPatient.getName());
            inputAddress.setText(selectedPatient.getHomeAddress());
            inputContactNumber.setText(selectedPatient.getContactNumber());
            inputMRNNo.setText(selectedPatient.getMRN());

            if(selectedPatient.getGender().equals("Male")){
                radioGenderMale.setSelected(true);
            }
            else{
                radioGenderFemale.setSelected(true);
            }

            inputCompanyName.setValue(selectedPatient.getCompanyName());
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
        else{
            showSuccess("Oops!", "Select a patient from the table first.");
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
        btnResults.setDisable(true);
    }

    public void disableAddInputFields(boolean value){
        inputMRNNo.setDisable(value);
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
        btnClinicalMeasurements.setDisable(value);
        btnFamilyHealthHistory.setDisable(value);
        lblCompanyName.setDisable(value);
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
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        return loader;
    }

    public void packagesOnChange(){
        comboTestType.setDisable(true);
        radioCorporate.setDisable(false);
        radioSanitary.setDisable(false);
        inputOtherTests.setDisable(false);
        //disableRadioGraphic(true);
    }

    public void individualTestsOnChange(){
        comboTestType.setDisable(false);
        radioCorporate.setDisable(true);
        radioSanitary.setDisable(true);
        inputOtherTests.setDisable(true);
        //disableRadioGraphic(true);
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
        //disableRadioGraphic(false);
        btnAddPatientSave.setDisable(false);
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
        //disableRadioGraphic(false);
        btnAddPatientSave.setDisable(false);
    }

    public void onChangeComboOtherTests(){
        if(comboTestType.getValue() != null){
            String testChoice = comboTestType.getValue().toString();
            if(testChoice.equals("Radiographic")){
                radioGraphic = true;
                CBC = UA = FA = bloodChemistry = MISC = false;
                showSuccess("Notice", "Click save button and manually edit the word file if patient conditions are not normal.");
                //disableRadioGraphic(false);
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
            btnAddPatientSave.setDisable(false);
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
        inputMRNNo.setText("");

    }

    public void onClickBtnDelete(){
        Patient selectedPatient = tblPatients.getSelectionModel().getSelectedItem();
        if(selectedPatient != null){
            if(showDialogBox("Confirmation", "Are you sure you want to delete patient " + selectedPatient.getName() + " ?")){
                String deletePatient = "DELETE from PATIENTS WHERE MRN = ?";
                String deleteSecondaryInfo = "DELETE from SecondaryInfo where MRNID = ?";
                String patientMRN = selectedPatient.getMRN();
                DBHelper.executeQuery(deletePatient, new String[]{patientMRN}, StatementType.DELETE);
                DBHelper.executeQuery(deleteSecondaryInfo, new String[]{patientMRN}, StatementType.DELETE);
                patients.remove(selectedPatient);
                tblPatients.refresh();
            }
        }
        else{
            showSuccess("Oops!", "Select a patient from the table first.");
        }
    }

    public void onClickBtnPrint() throws IOException{
        Patient selectedPatient = tblPatients.getSelectionModel().getSelectedItem();
        if(selectedPatient != null) {
            String companyName = selectedPatient.getCompanyName();
            String name = selectedPatient.getName();
            String date = selectedPatient.getDate();
            String mrn = selectedPatient.getMRN();

            Desktop desktop= Desktop.getDesktop();
            String directory = "files/"+companyName+"/"+mrn+" "+name+"/";

            File[] files = new File(directory).listFiles();
            for(File file : files){
                if(file.isFile()){
                    if (desktop.isSupported(Desktop.Action.PRINT)){
                        desktop.print(file);
                    }
                }
            }
        }
        else{
            showSuccess("Oops!", "Select a patient from the table first.");
        }
    }
}
