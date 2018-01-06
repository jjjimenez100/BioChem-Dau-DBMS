package ph.biochem.modules;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ConfigManagement {
    private boolean corporate, sanitary, individual;
    private boolean debugMode;
    private PrintWriter output;
    private String directoryPath, name, companyName, otherTests;

    public ConfigManagement(boolean corporate, boolean sanitary, boolean individual, String directoryPath, String name, String companyName, String otherTests) {
        this.corporate = corporate;
        this.sanitary = sanitary;
        this.individual = individual;
        debugMode = true;
        this.directoryPath = directoryPath;
        this.name = name;
        this.companyName = companyName;
        this.otherTests = otherTests;
        createConfigPackages();
    }

    private void createConfigPackages(){
        createNewTextFile(directoryPath + "/packages.biochem");
        if(corporate){
            writeToTextFile(new String[]{name, companyName, "true", "false", "false", otherTests});
        }
        else if(sanitary){
            writeToTextFile(new String[]{name, companyName, "false", "true", "false", otherTests});
        }
        else{
            writeToTextFile(new String[]{name, companyName, "false", "false", "true", otherTests});
        }
        closeIOStream();
    }

    public void createConfigTest(String fileName, String[] values){
        createNewTextFile(directoryPath + fileName);
        writeToTextFile(values);
        closeIOStream();
    }

    private void createNewTextFile(String fileName){
        try{
            output = new PrintWriter(fileName);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void writeToTextFile(String[] blockOfText){
        int size = blockOfText.length;
        for(int index=0; index<size; index++){
            if(index == size-1){
                output.print(blockOfText[index]);
            }
            else{
                output.println(blockOfText[index]);
            }
        }
    }

    private void closeIOStream(){
        if(output != null){
            output.close();
        }
    }

    public boolean isCorporate() {
        return corporate;
    }

    public void setCorporate(boolean corporate) {
        this.corporate = corporate;
    }

    public boolean isSanitary() {
        return sanitary;
    }

    public void setSanitary(boolean sanitary) {
        this.sanitary = sanitary;
    }

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
    }
}
