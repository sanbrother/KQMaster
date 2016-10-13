using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading;
using System.IO;

namespace MasterService
{
    public partial class MasterService : ServiceBase
    {
        private Thread thread = null;

        // 6 minutes
        private static Int32 JOB_INTERVAL = 6 * 60 * 1000;

        private static String JAR_FILE = @"kMaster.jar";

        public MasterService()
        {
            InitializeComponent();
        }

        private static void threadFunc()
        {
            while (true)
            {
                CommonService.LogError("doing job");

                ExecuteCommand();

                Thread.Sleep(JOB_INTERVAL);
            }
        }

        public static string GetCommandFullPath(string filename)
        {
            return new[] { Environment.CurrentDirectory }
                .Concat(Environment.GetEnvironmentVariable("PATH").Split(';'))
                .Select(dir => Path.Combine(dir, filename))
                .FirstOrDefault(path => File.Exists(path));
        }

        public static int ExecuteCommand()
        {
            int ExitCode;
            ProcessStartInfo ProcessInfo;
            Process Process;
            String serviceBinPath = Path.GetDirectoryName(System.Reflection.Assembly.GetEntryAssembly().Location);

            ProcessInfo = new ProcessStartInfo(GetCommandFullPath("java.exe"), @"-jar" + " \"" + serviceBinPath + "/" + JAR_FILE + "\" " + " \"" + serviceBinPath + "\"");
            ProcessInfo.CreateNoWindow = true;
            ProcessInfo.UseShellExecute = false;
            ProcessInfo.RedirectStandardOutput = true;

            Process = Process.Start(ProcessInfo);

            using (StreamReader reader = Process.StandardOutput)
            {
                string result = reader.ReadToEnd();
                Console.Write(result);
            }

            Process.WaitForExit();

            ExitCode = Process.ExitCode;

            Process.Close();

            return ExitCode;
        }

        protected override void OnStart(string[] args)
        {
            this.thread = new Thread(new ThreadStart(threadFunc));
            this.thread.Start();

            CommonService.LogError("onStart " + Path.GetDirectoryName(System.Reflection.Assembly.GetEntryAssembly().Location));
        }

        protected override void OnStop()
        {
            try
            {
                this.thread.Abort();
            }
            catch (ThreadAbortException)
            {

            }

            this.thread = null;

            CommonService.LogError("onStop");
        }
    }
}
