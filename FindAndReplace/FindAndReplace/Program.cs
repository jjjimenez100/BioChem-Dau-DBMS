using System.IO;
using System.Diagnostics;

namespace FindAndReplace
{
    class Program
    {
        static StreamReader mainConfig;
        static StreamReader CBCResults, UAResults, FAResults, RadioGraphic;
        static StreamReader PersonalInfo;
        static StreamReader template;
        static StreamReader gend;
        static StreamReader MRN;
        static StreamReader dates;
        static string templateDirectory = "templates/";
        static string outputDirectory = "../../../../files/";
        static void Main(string[] args)
        {
            //string inputFile = "FA.docx";
            //string outputFile = "Sample Output.docx";

            //// Copy Word document.
            //File.Copy(inputFile, outputFile, true);

            //// Open copied document.
            //using (var flatDocument = new FlatDocument(outputFile))
            //{
            //    // Search and replace document's text content.
            //    flatDocument.FindAndReplace("JOSHUA", "1");
            //    // Save document on Dispose.
            //}
            mainConfig = new StreamReader("config/packages.biochem");
            MRN = new StreamReader("config/MRNNo.biochem");
            dates = new StreamReader("config/Dates.biochem");
            string line;
            bool corporate, sanitary, individual;
            corporate = sanitary = individual = false;
            string name = mainConfig.ReadLine();
            string companyName = mainConfig.ReadLine();

            string companyDirectory = outputDirectory + companyName;
            Directory.CreateDirectory(companyDirectory);

            string personDirectory = companyDirectory + "/" + MRN.ReadLine() + " " + name + "/";
            outputDirectory = personDirectory;
            Directory.CreateDirectory(outputDirectory);
            if (mainConfig.ReadLine().Equals("true"))
            {
                corporate = true;
                mainConfig.ReadLine();
                mainConfig.ReadLine();
            }
            else if (mainConfig.ReadLine().Equals("true"))
            {
                sanitary = true;
                mainConfig.ReadLine();
            }
            else if (mainConfig.ReadLine().Equals("true"))
            {
                individual = true;
            }

            PersonalInfo = new StreamReader("config/PersonalInfo.biochem");
            if (corporate)
            {
                string inputFile = "Corporate.docx";
                CBCResults = new StreamReader("config/CBC.biochem");
                CBCResults.ReadLine();
                UAResults = new StreamReader("config/UA.biochem");
                FAResults = new StreamReader("config/FA.biochem");
                RadioGraphic = new StreamReader("config/Radiographic.biochem");
                
                string otherTests = mainConfig.ReadLine();

                replaceCorporate(PersonalInfo, CBCResults, UAResults, FAResults, RadioGraphic,
                    inputFile, name, otherTests);

                PersonalInfo.Close();
                PersonalInfo = new StreamReader("config/PersonalInfo.biochem");

                replaceRadioGraphic(PersonalInfo, RadioGraphic, "Radiographic.docx", name);
            }

            else if (sanitary)
            {
                string inputFile = "Sanitary.docx";  
                UAResults = new StreamReader("config/UA.biochem");
                FAResults = new StreamReader("config/FA.biochem");
                RadioGraphic = new StreamReader("config/Radiographic.biochem");
               
                string otherTests = mainConfig.ReadLine();

                replaceSanitary(PersonalInfo, UAResults, FAResults, RadioGraphic,
                    inputFile, name, otherTests);

                PersonalInfo.Close();
                PersonalInfo = new StreamReader("config/PersonalInfo.biochem");

                replaceRadioGraphic(PersonalInfo, RadioGraphic, "Radiographic.docx", name);
            }

            else if (individual)
            {
                mainConfig.ReadLine();
                string testType = mainConfig.ReadLine();
                if (testType.Equals("Radiographic"))
                {
                    RadioGraphic = new StreamReader("config/Radiographic.biochem");
                    replaceRadioGraphic(PersonalInfo, RadioGraphic, "Radiographic.docx", name);
                }
                else if (testType.Equals("CBC"))
                {
                    CBCResults = new StreamReader("config/CBC.biochem");
                    string cbcType = CBCResults.ReadLine();
                    if (cbcType.Equals("Adult"))
                    {
                        replaceAdultCBC(PersonalInfo, CBCResults, "AdultCBC.docx", name);
                    }
                    else if (cbcType.Equals("Pediatric"))
                    {
                        replacePediaCBC(PersonalInfo, CBCResults, "PediaCBC.docx", name);
                    }
                }
                else if (testType.Equals("FA"))
                {
                    FAResults = new StreamReader("config/FA.biochem");
                    replaceFA(PersonalInfo, FAResults, "FA.docx", name);
                }
                else if (testType.Equals("UA"))
                {
                    UAResults = new StreamReader("config/UA.biochem");
                    replaceUA(PersonalInfo, UAResults, "UA.docx", name);
                }
                else if(testType.Equals("Blood Chemistry"))
                {
                    StreamReader BloodChemistryResults = new StreamReader("config/BloodChemistry.biochem");
                    replaceBloodChem(PersonalInfo, BloodChemistryResults, "BloodChem.docx", name);
                }
                else if (testType.Equals("Misc"))
                {
                    StreamReader miscResults = new StreamReader("config/Misc.biochem");
                    replaceMISC(PersonalInfo, miscResults, "MISC.docx", name);
                }
            }
        }

        public static void replaceCorporate(StreamReader PersonalInfo, StreamReader CBC,
            StreamReader UA, StreamReader FA, StreamReader RadioGraphic, 
            string inputFile, string name, string otherTests)
        {
            gend = new StreamReader("config/Gender.biochem");
            string gender = gend.ReadLine();
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for(int index=1; index<=48; )
                {
                    if(index>=1 && index <= 6)
                    {
                        flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                    }
                    else if(index>=7 && index <= 22)
                    {
                        if (index == 11)
                        {
                            if (gender.Equals("Male"))
                            {
                                flatDocument.FindAndReplace("B11", isNull(CBC.ReadLine()));
                                flatDocument.FindAndReplace("B12", "");
                            }
                            else if (gender.Equals("Female"))
                            {
                                flatDocument.FindAndReplace("B12", isNull(CBC.ReadLine()));
                                flatDocument.FindAndReplace("B11", "");
                            }
                            index += 2;
                            continue;
                        }

                        else if (index == 15)
                        {
                            if (gender.Equals("Male"))
                            {
                                flatDocument.FindAndReplace("B15", isNull(CBC.ReadLine()));
                                flatDocument.FindAndReplace("B16", "");
                            }
                            else if (gender.Equals("Female"))
                            {
                                flatDocument.FindAndReplace("B16", isNull(CBC.ReadLine()));
                                flatDocument.FindAndReplace("B15", "");
                            }
                            index += 2;
                            continue;
                        }

                        else if (index == 17)
                        {
                            if (gender.Equals("Male"))
                            {
                                flatDocument.FindAndReplace("B17", isNull(CBC.ReadLine()));
                                flatDocument.FindAndReplace("B18", "");
                            }
                            else if (gender.Equals("Female"))
                            {
                                flatDocument.FindAndReplace("B18", isNull(CBC.ReadLine()));
                                flatDocument.FindAndReplace("B17", "");
                            }
                            index += 2;
                            continue;
                        }

                        else if (index == 19)
                        {
                            if (gender.Equals("Male"))
                            {
                                flatDocument.FindAndReplace("B19", isNull(CBC.ReadLine()));
                                flatDocument.FindAndReplace("B20", "");
                            }
                            else if (gender.Equals("Female"))
                            {
                                flatDocument.FindAndReplace("B20", isNull(CBC.ReadLine()));
                                flatDocument.FindAndReplace("B19", "");
                            }
                            index += 2;
                            continue;
                        }
                        else
                        {
                            flatDocument.FindAndReplace("B" + index.ToString(), isNull(CBC.ReadLine()));
                        }
                    }
                    else if (index >= 23 && index <= 39)
                    {
                            flatDocument.FindAndReplace("U" + index.ToString(), isNull(UA.ReadLine()));
                    }
                    else
                    {
                            flatDocument.FindAndReplace("F" + index.ToString(), isNull(FA.ReadLine()));
                    }
                    index++;
                }
                flatDocument.FindAndReplace("O49", isNull(otherTests));
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }
        }

        public static void replaceSanitary(StreamReader PersonalInfo, StreamReader UA, StreamReader FA, StreamReader RadioGraphic,
            string inputFile, string name, string otherTests)
        {
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for (int index = 1; index <= 44;)
                {
                    if (index >= 1 && index <= 6)
                    {
                        flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                    }
                    else if (index >= 19 && index <= 35)
                    {
                        flatDocument.FindAndReplace("U" + index.ToString(), isNull(UA.ReadLine()));
                    }
                    else if(index >= 36 && index <= 44)
                    {
                        flatDocument.FindAndReplace("F" + index.ToString(), isNull(FA.ReadLine()));
                    }
                    index++;
                }
                flatDocument.FindAndReplace("O45", isNull(otherTests));
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }

        }

        public static void replaceRadioGraphic(StreamReader PersonalInfo, StreamReader RadioGraphic,
            string inputFile, string name)
        {
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for(int index=1; index<=6; index++)
                {
                        flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                }
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }
        }

        public static void replacePediaCBC(StreamReader PersonalInfo, StreamReader CBC, string inputFile, string name)
        {
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for (int index = 1; index <= 18;)
                {
                    if (index >= 1 && index <= 6)
                    {
                        flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                    }
                    else if (index >= 7 && index <= 18)
                    {
                        flatDocument.FindAndReplace("B" + index.ToString(), isNull(CBC.ReadLine()));
                    }
                    index++;
                }
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }
        }

        public static void replaceAdultCBC(StreamReader PersonalInfo, StreamReader CBC, string inputFile, string name)
        {
            gend = new StreamReader("config/Gender.biochem");
            string gender = gend.ReadLine();
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for (int index = 1; index >= 1 && index <= 6; index++)
                {
                    flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                }
                for (int index = 7; index <= 22;)
                {
                    if (index == 11)
                    {
                        if (gender.Equals("Male"))
                        {
                            flatDocument.FindAndReplace("B11", isNull(CBC.ReadLine()));
                            flatDocument.FindAndReplace("B12", "");
                        }
                        else if (gender.Equals("Female"))
                        {
                            flatDocument.FindAndReplace("B12", isNull(CBC.ReadLine()));
                            flatDocument.FindAndReplace("B11", "");
                        }
                        index += 2;
                        continue;
                    }

                    else if (index == 15)
                    {
                        if (gender.Equals("Male"))
                        {
                            flatDocument.FindAndReplace("B15", isNull(CBC.ReadLine()));
                            flatDocument.FindAndReplace("B16", "");
                        }
                        else if (gender.Equals("Female"))
                        {
                            flatDocument.FindAndReplace("B16", isNull(CBC.ReadLine()));
                            flatDocument.FindAndReplace("B15", "");
                        }
                        index += 2;
                        continue;
                    }

                    else if (index == 17)
                    {
                        if (gender.Equals("Male"))
                        {
                            flatDocument.FindAndReplace("B17", isNull(CBC.ReadLine()));
                            flatDocument.FindAndReplace("B18", "");
                        }
                        else if (gender.Equals("Female"))
                        {
                            flatDocument.FindAndReplace("B18", isNull(CBC.ReadLine()));
                            flatDocument.FindAndReplace("B17", "");
                        }
                        index += 2;
                        continue;
                    }

                    else if (index == 19)
                    {
                        if (gender.Equals("Male"))
                        {
                            flatDocument.FindAndReplace("B19", isNull(CBC.ReadLine()));
                            flatDocument.FindAndReplace("B20", "");
                        }
                        else if (gender.Equals("Female"))
                        {
                            flatDocument.FindAndReplace("B20", isNull(CBC.ReadLine()));
                            flatDocument.FindAndReplace("B19", "");
                        }
                        index += 2;
                        continue;
                    }
                    else
                    {
                        flatDocument.FindAndReplace("B" + index.ToString(), isNull(CBC.ReadLine()));
                    }
                    index++;
                }
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }
        }

        public static void replaceBloodChem(StreamReader PersonalInfo, StreamReader BloodChem, string inputFile, string name)
        {
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for (int index = 1; index <= 6; index++)
                {
                   flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                }

                for(int index=1; index<=9; index++)
                {
                    flatDocument.FindAndReplace("BC" + index.ToString(), isNull(BloodChem.ReadLine()));
                }
                flatDocument.FindAndReplace("BCSGO" + 10.ToString(), isNull(BloodChem.ReadLine()));
                for (int index = 11; index <= 19; index++)
                {
                    flatDocument.FindAndReplace("BCS" + index.ToString(), isNull(BloodChem.ReadLine()));
                }
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }
        }

        public static void replaceFA(StreamReader PersonalInfo, StreamReader FA, string inputFile, string name)
        {
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for (int index = 1; index <= 6; index++)
                {
                    flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                }

                for(int index = 36; index <= 44; index++)
                {
                    flatDocument.FindAndReplace("F" + index.ToString(), isNull(FA.ReadLine()));
                }
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }
        }

        public static void replaceUA(StreamReader PersonalInfo, StreamReader UA, string inputFile, string name)
        {
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for (int index = 1; index <= 6; index++)
                {
                    flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                }

                for (int index = 19; index <= 35; index++)
                {
                    flatDocument.FindAndReplace("U" + index.ToString(), isNull(UA.ReadLine()));
                }
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }
        }

        public static void replaceMISC(StreamReader PersonalInfo, StreamReader MISC, string inputFile, string name)
        {
            string outputFile = outputDirectory + name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(templateDirectory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for (int index = 1; index <= 6; index++)
                {
                    flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                }

                flatDocument.FindAndReplace("MISC1", isNull(MISC.ReadLine()));
                flatDocument.FindAndReplace("MISC2", isNull(MISC.ReadLine()));
                flatDocument.FindAndReplace("DATE1", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE2", isNull(dates.ReadLine()));
                flatDocument.FindAndReplace("DATE3", isNull(dates.ReadLine()));
            }
        }

        public static string isNull(string o)
        {
            if(o == null)
            {
                return "";
            }
            else
            {
                return o;
            }
        }
    }
}
