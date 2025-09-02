package com.eurest.supplier.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.SystemUtils;
import org.springframework.stereotype.Component;

@Component
public class DownloadSATUtils {
	
	public static int GetChromeDriverProcessID(int aPort) throws IOException, InterruptedException
	  {
	    String[] commandArray = new String[3];

	    if (SystemUtils.IS_OS_LINUX)
	    {
	      commandArray[0] = "/bin/sh";
	      commandArray[1] = "-c";
	      commandArray[2] = "netstat -anp | grep LISTEN | grep " + aPort;
	    }
	    else if (SystemUtils.IS_OS_WINDOWS)
	    {
	      commandArray[0] = "cmd";
	      commandArray[1] = "/c";
	      commandArray[2] = "netstat -aon | findstr LISTENING | findstr " + aPort;
	    }
	    else
	    {
	      System.out.println("platform not supported");
	      System.exit(-1);
	    }

	    System.out.println("running command " + commandArray[2]);

	    Process p = Runtime.getRuntime().exec(commandArray);
	    p.waitFor();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

	    StringBuilder sb = new StringBuilder();
	    String line = "";
	    while ((line = reader.readLine()) != null)
	    {
	      sb.append(line + "\n");
	    }

	    String result = sb.toString().trim();

	    System.out.println("parse command response line:");
	    System.out.println(result);

	    return SystemUtils.IS_OS_LINUX ? ParseChromeDriverLinux(result) : ParseChromeDriverWindows(result);
	  }

	public static int GetChromeProcesID(int chromeDriverProcessID) throws IOException, InterruptedException
	  {
	    String[] commandArray = new String[3];

	    if (SystemUtils.IS_OS_LINUX)
	    {
	      commandArray[0] = "/bin/sh";
	      commandArray[1] = "-c";
	      commandArray[2] = "ps -efj | grep google-chrome | grep " + chromeDriverProcessID;
	    }
	    else if (SystemUtils.IS_OS_WINDOWS)
	    {
	      commandArray[0] = "cmd";
	      commandArray[1] = "/c";
	      commandArray[2] = "wmic process get processid,parentprocessid,executablepath | find \"chrome.exe\" |find \"" + chromeDriverProcessID + "\"";
	    }
	    else
	    {
	      System.out.println("platform not supported");
	      System.exit(-1);
	    }

	    System.out.println("running command " + commandArray[2]);

	    Process p = Runtime.getRuntime().exec(commandArray);
	    p.waitFor();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

	    StringBuilder sb = new StringBuilder();
	    String line = "";
	    while ((line = reader.readLine()) != null)
	    {
	      if (SystemUtils.IS_OS_LINUX && line.contains("/bin/sh"))
	      {
	        continue;
	      }

	      sb.append(line + "\n");
	    }

	    String result = sb.toString().trim();

	    System.out.println("parse command response line:");
	    System.out.println(result);

	    return SystemUtils.IS_OS_LINUX ? ParseChromeLinux(result) : ParseChromeWindows(result);
	  }
	  private static int ParseChromeLinux(String result)
	  {
	    String[] pieces = result.split("\\s+");
	    // root 20780 20772 20759 15980  9 11:04 pts/1    00:00:00 /opt/google/chrome/google-chrome.........
	    // the second one is the chrome process id
	    return Integer.parseInt(pieces[1]);
	  }

	  private static int ParseChromeWindows(String result)
	  {
	    String[] pieces = result.split("\\s+");
	    // C:\Program Files (x86)\Google\Chrome\Application\chrome.exe 14304 19960
	    return Integer.parseInt(pieces[pieces.length - 1]);
	  }

	  private static int ParseChromeDriverLinux(String netstatResult)
	  {
	    String[] pieces = netstatResult.split("\\s+");
	    String last = pieces[pieces.length - 1];
	    // tcp 0 0 127.0.0.1:2391 0.0.0.0:* LISTEN 3333/chromedriver
	    return Integer.parseInt(last.substring(0, last.indexOf('/')));
	  }

	  private static int ParseChromeDriverWindows(String netstatResult)
	  {
	    String[] pieces = netstatResult.split("\\s+");
	    // TCP 127.0.0.1:26599 0.0.0.0:0 LISTENING 22828
	    return Integer.parseInt(pieces[pieces.length - 1]);
	  }

}
