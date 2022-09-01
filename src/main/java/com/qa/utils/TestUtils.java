package com.qa.utils;

import com.qa.BaseTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestUtils {

    //TestUtils utils = new TestUtils();

    public static final long WAIT = 10;

    public HashMap<String, String> parseStringXML(InputStream file) throws Exception {
        HashMap<String, String > stringMap = new HashMap<String, String>();

        //Get document builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //build document
        Document document = builder.parse(file);

        //Normalize the XML structure
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element root = document.getDocumentElement();


        //get all elements
        NodeList nlist = document.getElementsByTagName("string");

        for(int temp=0; temp< nlist.getLength(); temp++){
            Node node = nlist.item(temp);
            if(node.getNodeType()==Node.ELEMENT_NODE){
                Element eElement = (Element) node;
                //Store each element keyvalue in map
                stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());
            }
        }
        return stringMap;
    }

    public String dateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        //utils.log(dateFormat.format(date));
        return dateFormat.format(date);
    }

    public void log(String txt) {
        BaseTest base = new BaseTest();
        String msg = Thread.currentThread().getId() + ":" + base.getDeviceName() + ":"
                + Thread.currentThread().getStackTrace()[2].getClassName() + ":" + txt;

        System.out.println(msg);

        String strFile = "logs" + File.separator  + "_" + base.getDeviceName()
                + File.separator + base.getDateTime();

        File logFile = new File(strFile);

        if (!logFile.exists()) {
            logFile.mkdirs();
        }

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile + File.separator + "log.txt",true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(msg);
        printWriter.close();
    }

    public Logger log() {
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }
}
