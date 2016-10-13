using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace MasterService
{
    public class CommonService
    {
        public static void LogError(String msg)
        {
            try
            {
                String serviceBinPath = Path.GetDirectoryName(System.Reflection.Assembly.GetEntryAssembly().Location);
                String LogFile = String.Format("{0}\\errorlog.txt", serviceBinPath);

                if (LogFile != "")
                {
                    String Message = String.Format(@"{0}====================== {1} ====================={0}{2}{0}" ,Environment.NewLine, DateTime.Now, msg);
                    byte[] binLogString = Encoding.Default.GetBytes(Message);

                    System.IO.FileStream logFile = new System.IO.FileStream(LogFile,
                              System.IO.FileMode.OpenOrCreate,
                              System.IO.FileAccess.Write,
                              System.IO.FileShare.Write);
                    logFile.Seek(0, System.IO.SeekOrigin.End);
                    logFile.Write(binLogString, 0, binLogString.Length);
                    logFile.Close();
                }
            }
            catch { ; }
        }
    }
}
