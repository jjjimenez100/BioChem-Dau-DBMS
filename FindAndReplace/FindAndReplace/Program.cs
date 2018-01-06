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
            string line;
            bool corporate, sanitary, individual;
            corporate = sanitary = individual = false;
            string name = mainConfig.ReadLine();
            string companyName = mainConfig.ReadLine();

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

            if (corporate)
            {
                string inputFile = "Corporate.docx";
                PersonalInfo = new StreamReader("config/PersonalInfo.biochem");
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
                PersonalInfo = new StreamReader("config/PersonalInfo.biochem");
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
        }

        public static void replaceInfo(StreamReader source, string inputFile, 
            string name, int start, int max, string suffix)
        {
            string directory = "templates/";
            string outputFile = name + " " + inputFile;
            File.Copy(directory+inputFile, outputFile, true);

            using (var flatDocument = new FlatDocument(outputFile))
            {
                for (; start <= max; start++)
                {
                    flatDocument.FindAndReplace(suffix+start.ToString(), source.ReadLine());
                }
            }
            
        }

        public static void replaceCorporate(StreamReader PersonalInfo, StreamReader CBC,
            StreamReader UA, StreamReader FA, StreamReader RadioGraphic, 
            string inputFile, string name, string otherTests)
        {
            string directory = "templates/";
            string outputFile = name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(directory + inputFile, outputFile, true);
            //CBC.ReadLine(); //adult or pedia
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for(int index=1; index<=44; )
                {
                    string holder;
                    if(index>=1 && index <= 6)
                    {
                        flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                    }
                    else if(index>=7 && index <= 18)
                    {
                            flatDocument.FindAndReplace("B" + index.ToString(), isNull(CBC.ReadLine()));
                    }
                    else if (index >= 19 && index <= 35)
                    {
                            flatDocument.FindAndReplace("U" + index.ToString(), isNull(UA.ReadLine()));
                    }
                    else
                    {
                            flatDocument.FindAndReplace("F" + index.ToString(), isNull(FA.ReadLine()));
                    }
                    index++;
                }
                flatDocument.FindAndReplace("O45", isNull(otherTests));
            }

        }

        public static void replaceSanitary(StreamReader PersonalInfo, StreamReader UA, StreamReader FA, StreamReader RadioGraphic,
            string inputFile, string name, string otherTests)
        {
            string directory = "templates/";
            string outputFile = name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(directory + inputFile, outputFile, true);
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
            }

        }

        public static void replaceRadioGraphic(StreamReader PersonalInfo, StreamReader RadioGraphic,
            string inputFile, string name)
        {
            string directory = "templates/";
            string outputFile = name + " " + System.DateTime.Today.ToString("M-d-yyyy") + " " + inputFile;
            File.Copy(directory + inputFile, outputFile, true);
            using (var flatDocument = new FlatDocument(outputFile))
            {
                for(int index=1; index<=6; index++)
                {
                        flatDocument.FindAndReplace("P" + index.ToString(), PersonalInfo.ReadLine());
                }
                RadioGraphic.ReadLine(); //start
                string holder = isNull(RadioGraphic.ReadLine());
                while (!holder.Equals("END"))
                {
                    flatDocument.FindAndReplace("C1", holder + "\nC1");
                    holder = isNull(RadioGraphic.ReadLine());
                }
                flatDocument.FindAndReplace("C1", "");
                flatDocument.FindAndReplace("C2", isNull(RadioGraphic.ReadLine()));
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
