package com.mauro.caredda;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyRouteBuilder extends RouteBuilder {

    Processor setFileName = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String filename = "SplitFile_#" + timeStamp + "_#" + ".xml";
            exchange.getIn().setHeader("CamelFilename",filename);
        }
    };


    public void configure() {

     from("file:C:/test/input?noop=true")
             .choice()
             .when(header("CamelFileName").endsWith(".txt"))
                .to("file:C:/test/output")
             .when(header("CamelFilename").endsWith(".xml"))
                .split().tokenizeXML("Order","*")
                .process(setFileName)
             .to("file:C:/test/outputXML");
    }

}
