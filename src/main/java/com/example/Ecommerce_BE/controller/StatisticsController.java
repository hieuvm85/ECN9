package com.example.Ecommerce_BE.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.model.entity.Cart;
import com.example.Ecommerce_BE.model.entity.EPaymentOption;
import com.example.Ecommerce_BE.model.entity.EStatusOrder;
import com.example.Ecommerce_BE.model.entity.Order;
import com.example.Ecommerce_BE.model.entity.Product;
import com.example.Ecommerce_BE.model.service.OrderService;
import com.example.Ecommerce_BE.model.service.StatisticsService;
import com.example.Ecommerce_BE.payload.request.EType;
import com.example.Ecommerce_BE.payload.request.StatisticsRequest;
import com.example.Ecommerce_BE.payload.request.TimeZoneRequest;
import com.example.Ecommerce_BE.payload.response.DataChartResponse;
import com.example.Ecommerce_BE.payload.response.MessageResponse;
import com.example.Ecommerce_BE.payload.response.Transport;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.*;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {
	
	@Autowired
	StatisticsService statisticsService;
	@Autowired
	OrderService orderService;
	
	@PostMapping("/interest")
	public ResponseEntity<?> getInterest(@RequestBody StatisticsRequest statisticsRequest){
		List<LocalDate[]> dates = statisticsRequest.splitDates();
		DataChartResponse dataChartResponse = new DataChartResponse();
		List<LocalDate> labels = new ArrayList<>();

		List<Double> values = new ArrayList<>(); 
		
		
		for(LocalDate[] date :dates) {
			labels.add(date[0]);
			values.add(statisticsService.getInterestbyDate(date[0], date[1]));
		}
		dataChartResponse.setLabel(labels);
		dataChartResponse.setValue(values);
		return ResponseEntity.ok( dataChartResponse );
	}
	
	
	private Transport calculateTransportFee(StatisticsRequest timeZoneRequest) {
	    LocalDateTime startDateTime = timeZoneRequest.getStartDate().atStartOfDay();
	    LocalDateTime endDateTime = timeZoneRequest.getEndDate().atTime(LocalTime.MAX);
	    int totalPayCashOrder = 0;
	    int totalPayWalletOrder = 0;
	    int totalMoneyItemPayCashOrder = 0;
	    int totalMoneyItemPayWalletOrder = 0;
	    int totalCancelOrder = 0;
	    int totalMoneyShipCancelOrder = 0;
	    int totalMoneyShipPayCashOrder = 0;
	    int totalMoneyShipPayWalletOrder = 0;
	    int amountReceivable = 0;
	    List<Order> orderDelevereds = orderService.getByEstatusOrderAndCreatedBetween(EStatusOrder.DELEVERED, startDateTime, endDateTime);
	    for (Order order : orderDelevereds) {
	        if (order.getPaymentOption() == EPaymentOption.PAY_CASH) {
	            totalPayCashOrder++;
	            totalMoneyItemPayCashOrder += order.getAmount();
	            totalMoneyShipPayCashOrder += order.getAmount();
	        } else {
	            totalPayWalletOrder++;
	            totalMoneyItemPayWalletOrder += order.getAmount();
	            totalMoneyShipPayWalletOrder += order.getAmount();
	        }
	    }
	    List<Order> orderCancels = orderService.getByEstatusOrderAndCreatedBetween(EStatusOrder.CANCELLED, startDateTime, endDateTime);
	    totalCancelOrder = orderCancels.size();
	    for (Order order : orderCancels) {
	        totalMoneyShipCancelOrder += order.getAmount() * 2;
	    }
	    amountReceivable = totalMoneyItemPayCashOrder - totalMoneyShipPayWalletOrder - totalMoneyShipCancelOrder;
	    return new Transport(totalPayCashOrder, totalPayWalletOrder, totalMoneyItemPayCashOrder, totalMoneyItemPayWalletOrder,
	            totalCancelOrder, totalMoneyShipCancelOrder, totalMoneyShipPayCashOrder, totalMoneyShipPayWalletOrder, amountReceivable);
	}
	
	
	@PostMapping("/transportFee")
	public ResponseEntity<?> getTransportFee(@RequestBody StatisticsRequest timeZoneRequest) {
	    Transport transport = calculateTransportFee(timeZoneRequest);
	    transport.setStartDate(timeZoneRequest.getStartDate());
	    transport.setEndDate(timeZoneRequest.getEndDate());
	    return ResponseEntity.ok(transport);
	}
	
	

//	// dung cai nay
//	@PostMapping("/export/pdf/transportFee")
//	public ResponseEntity<?> getExportPdfTransportFee(@RequestBody StatisticsRequest timeZoneRequest) {
//	    Transport transport = calculateTransportFee(timeZoneRequest);
//	    
//	    try {
//		    ClassPathResource pdfTemplate = new ClassPathResource("templateE.pdf");
//	        InputStream pdfInputStream = pdfTemplate.getInputStream();
//	        PDDocument document = PDDocument.load(pdfInputStream);
//	
//	        // Lấy toàn bộ nội dung của file PDF
//	        PDFTextStripper pdfStripper = new PDFTextStripper();
//	        String text = pdfStripper.getText(document);
//	        
//	        
//	        LocalDate date = LocalDate.now(); 
//	        
//	        text = text.replace("{{1}}",Integer.toString(date.getDayOfMonth()));
//	        text = text.replace("{{2}}",Integer.toString(date.getMonthValue()));
//	        text = text.replace("{{3}}",Integer.toString(date.getYear()));
//	        text = text.replace("{{4}}",timeZoneRequest.getStartDate().toString());
//	        text = text.replace("{{5}}",timeZoneRequest.getEndDate().toString() );
//	        text = text.replace("{{6}}",Integer.toString(transport.getTotalPayCashOrder()));
//	        text = text.replace("{{7}}",Integer.toString(transport.getTotalMoneyItemPayCashOrder()));
//	        text = text.replace("{{8}}",Integer.toString(transport.getTotalPayWalletOrder()));
//	        text = text.replace("{{9}}",Integer.toString(transport.getTotalMoneyItemPayWalletOrder()));
//	        text = text.replace("{{10}}",Integer.toString(transport.getTotalCancelOrder()));
//	        text = text.replace("{{11}}",Integer.toString(transport.getTotalMoneyShipCancelOrder()));
//	        text = text.replace("{{12}}",Integer.toString(transport.getTotalMoneyShipPayCashOrder()));
//	        text = text.replace("{{13}}",Integer.toString(transport.getTotalMoneyShipPayWalletOrder()));
//	        text = text.replace("{{14}}",Integer.toString(transport.getAmountReceivable()));
//	         
//	        
//	        System.out.println(text);
//	        
//
//	        
//	        
//	        // Lưu file PDF đã chỉnh sửa vào bộ nhớ tạm thời
////	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////	        document.save(byteArrayOutputStream);
////	        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
////	        
////	        
////	        HttpHeaders headers = new HttpHeaders();
////	        headers.setContentType(MediaType.APPLICATION_PDF);
////	        headers.setContentDispositionFormData("attachment", "exported_report.pdf");
////	        headers.setContentLength(pdfBytes.length);
////	        
////	        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//	        
//	        ByteArrayOutputStream baos = createPDF(text);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("filename", "generated.pdf");
//            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
//	        
//
//	    }catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//	}
//	private ByteArrayOutputStream createPDF(String text) throws IOException {
//		text = text.replace("\n", "").replace("\r", "");
//        try (PDDocument document = new PDDocument();
//             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            PDPageContentStream contentStream = new PDPageContentStream(document, page);
//            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
//            contentStream.beginText();
//            contentStream.newLineAtOffset(100, 700); 
//            contentStream.showText(text);
//            contentStream.endText();
//            contentStream.close();
//
//            document.save(baos);
//            return baos;
//        }
//    }
	@PostMapping("/export/pdf/transportFee")
	public ResponseEntity<?> getExportPdfTransportFee(@RequestBody StatisticsRequest timeZoneRequest) throws FileNotFoundException {
	   
	   
	// Create a ByteArrayOutputStream to store the generated PDF content
       ByteArrayOutputStream baos = new ByteArrayOutputStream();

       // Create a PdfDocument with PdfWriter
       PdfWriter writer = new PdfWriter(baos);
       PdfDocument pdf = new PdfDocument(writer);

       // Create a Document
       Document document = new Document(pdf);

       // Add content to the PDF document based on your template
       addContentToPdf(document,timeZoneRequest);

       // Close the Document
       document.close();

       // Convert ByteArrayOutputStream to byte array
       byte[] pdfContents = baos.toByteArray();

       // Set HttpHeaders to specify content type as application/pdf and content disposition as attachment
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_PDF);
       headers.setContentDispositionFormData("attachment", "statistical_report.pdf");

       // Return ResponseEntity with the PDF byte array and headers
       return ResponseEntity.ok()
               .headers(headers)
               .body(pdfContents);
	}
	
	private void addContentToPdf(Document document,StatisticsRequest timeZoneRequest) {
		Transport transport = calculateTransportFee(timeZoneRequest);
        // Replace placeholders with actual data
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Add content to the PDF document based on your template
        // Center align and bold text
        
        Paragraph headerCom = new Paragraph()
                .setTextAlignment(TextAlignment.LEFT)
                .setBold()
                .add("ECN9 Software Company");
        document.add(headerCom);
        
        Paragraph header = new Paragraph()
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .add("SOCIALIST REPUBLIC OF VIETNAM\nIndependence - Freedom - Happiness\n…………");
        document.add(header);


        document.add(new Paragraph());
        Paragraph numParagraph = new Paragraph("Num: ……..  Ha Noi, " + currentDate)
                .setTextAlignment(TextAlignment.RIGHT); // Align left
        document.add(numParagraph);

        document.add(new Paragraph());

        Paragraph reportTitle = new Paragraph()
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .add("STATISTICAL REPORT ON PAYMENT OF SHIPPING COSTS");
        document.add(reportTitle);

        // Create a table with 2 columns
        Table table = new Table(2);
        table.setWidthPercent(100);

        // Adding rows to the table with data
        table.addCell("Start day");
        table.addCell(timeZoneRequest.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        table.addCell("End date");
        table.addCell(timeZoneRequest.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        table.addCell("Total number of orders paid in cash");
        table.addCell(Integer.toString(transport.getTotalPayCashOrder()));
        table.addCell("Total cost of orders paid in cash");
        table.addCell(Integer.toString(transport.getTotalMoneyItemPayCashOrder())+" vnd");
        table.addCell("Total number of orders paid by e-wallet");
        table.addCell(Integer.toString(transport.getTotalPayWalletOrder()));
        table.addCell("Total amount for orders paid by e-wallet");
        table.addCell(Integer.toString(transport.getTotalMoneyItemPayWalletOrder())+" vnd");
        table.addCell("Total number of orders returned");
        table.addCell(Integer.toString(transport.getTotalCancelOrder()));
        table.addCell("Total shipping cost of returned orders");
        table.addCell(Integer.toString(transport.getTotalMoneyShipCancelOrder())+" vnd");
        table.addCell("Total shipping cost for orders paid by cash");
        table.addCell(Integer.toString(transport.getTotalMoneyShipPayCashOrder())+" vnd");
        table.addCell("Total shipping cost of orders paid by e-wallet");
        table.addCell(Integer.toString(transport.getTotalMoneyShipPayWalletOrder())+" vnd");
        table.addCell("Total amount receivable from the carrier");
        table.addCell(Integer.toString(transport.getAmountReceivable())+" vnd");
        // Add the table to the document
        document.add(table);

        document.add(new Paragraph());
        document.add(new Paragraph("Comment:………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………"));
        Paragraph directorParagraph = new Paragraph("Director of ECN9 Company")
        		.setBold()
                .setTextAlignment(TextAlignment.RIGHT); // Align left
        document.add(directorParagraph);
    }
	
	
	
	
	
	
	 
	
	
	
	
	
	
	
	
	
	
	
	



	///báo cáo top 10 sản phẩm bán chạy
	@PostMapping("/top/products")
	public ResponseEntity<?> getTopProducts(@RequestBody StatisticsRequest timeZoneRequest){
		LocalDateTime startDateTime = timeZoneRequest.getStartDate().atStartOfDay();
	    LocalDateTime endDateTime = timeZoneRequest.getEndDate().atTime(LocalTime.MAX);
		List<Order> orders = orderService.getByCreatedBetween(startDateTime, endDateTime);
		Map<String, Integer> productQuantityMap = new HashMap<>();
		for(Order order: orders) {
			for(Cart cart: order.getCarts()) {
				String nameProduct= cart.getProduct().getTitle();
				int quantity=cart.getQuantity();
				productQuantityMap.put(nameProduct, productQuantityMap.getOrDefault(nameProduct, 0) + quantity);
			}
		}
		 List<Map.Entry<String, Integer>> sortedProductList = new ArrayList<>(productQuantityMap.entrySet());
	     Collections.sort(sortedProductList, new Comparator<Map.Entry<String, Integer>>() {
	            @Override
	            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
	                return entry2.getValue().compareTo(entry1.getValue()); // So sánh giá trị giảm dần
	            }
	        });
	    DataChartResponse dataChartResponse= new DataChartResponse(); 
	    int count = 0;
	    List<String> label = new  ArrayList<>();
	    List<Integer> data = new  ArrayList<>();
	    for (Map.Entry<String, Integer> entry : sortedProductList) {
	        if (count >= 10) {
	            break; // lấy đủ 10 sản phẩm
	        }
	        System.out.println("Sản phẩm: " + entry.getKey() + ", Tổng số lượng: " + entry.getValue());
	        count++;
	        label.add(entry.getKey());
	        data.add(entry.getValue());
	    }
	    dataChartResponse.setLabel(label);
	    dataChartResponse.setValue(data);
		return ResponseEntity.ok( dataChartResponse );
	}
	/// báo cáo top shop bán được nhiều đơn hàng 
	@PostMapping("/top/shops")
	public ResponseEntity<?> getTopShops(@RequestBody StatisticsRequest timeZoneRequest){
		LocalDateTime startDateTime = timeZoneRequest.getStartDate().atStartOfDay();
	    LocalDateTime endDateTime = timeZoneRequest.getEndDate().atTime(LocalTime.MAX);
		List<Order> orders = orderService.getByCreatedBetween(startDateTime, endDateTime);
		Map<String, Integer> productQuantityMap = new HashMap<>();
		for(Order order: orders) {
			String nameShop= order.getShop().getNameShop();
			for(Cart cart: order.getCarts()) {
				int quantity=cart.getQuantity();
				productQuantityMap.put(nameShop, productQuantityMap.getOrDefault(nameShop, 0) + quantity);
			}
		}
		 List<Map.Entry<String, Integer>> sortedProductList = new ArrayList<>(productQuantityMap.entrySet());
	     Collections.sort(sortedProductList, new Comparator<Map.Entry<String, Integer>>() {
	            @Override
	            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
	                return entry2.getValue().compareTo(entry1.getValue()); // So sánh giá trị giảm dần
	            }
	        });
	    DataChartResponse dataChartResponse= new DataChartResponse(); 
	    int count = 0;
	    List<String> label = new  ArrayList<>();
	    List<Integer> data = new  ArrayList<>();
	    for (Map.Entry<String, Integer> entry : sortedProductList) {
	        if (count >= 10) {
	            break; // lấy đủ 10 sản phẩm
	        }
	        System.out.println("Sản phẩm: " + entry.getKey() + ", Tổng số lượng: " + entry.getValue());
	        count++;
	        label.add(entry.getKey());
	        data.add(entry.getValue());
	    }
	    dataChartResponse.setLabel(label);
	    dataChartResponse.setValue(data);
		return ResponseEntity.ok( dataChartResponse );
	}
	 
	@PostMapping("/order")
	public ResponseEntity<?> getTopShops(){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime truncatedDateTime = currentTime.truncatedTo(ChronoUnit.MINUTES);
        // Xác định phút hiện tại
        int currentMinute = truncatedDateTime.getMinute();   
        // Tính toán phút cần điều chỉnh để chia hết cho 5
        int adjustedMinute = (currentMinute / 5) * 5;      
        // Tạo một đối tượng LocalDateTime mới với số phút đã điều chỉnh
        LocalDateTime nearestFiveMinuteDateTime = truncatedDateTime.withMinute(adjustedMinute);
        List<LocalTime> label =  new ArrayList<>();
        List<Integer> data =  new ArrayList<>();
        nearestFiveMinuteDateTime= nearestFiveMinuteDateTime.minusMinutes(45);
        for(int i=0;i<9;i++) {
        	data.add(orderService.countByCreatedBetween(nearestFiveMinuteDateTime, nearestFiveMinuteDateTime.plusMinutes(5)));
        	nearestFiveMinuteDateTime = nearestFiveMinuteDateTime.plusMinutes(5);
        	label.add(nearestFiveMinuteDateTime.toLocalTime());
        }
    	data.add(orderService.countByCreatedBetween(nearestFiveMinuteDateTime, currentTime));
    	label.add(currentTime.toLocalTime());
    	DataChartResponse dataChart =new DataChartResponse();
    	dataChart.setLabel(label);
    	dataChart.setValue(data);
        

		return ResponseEntity.ok( dataChart );
	}

	
}





