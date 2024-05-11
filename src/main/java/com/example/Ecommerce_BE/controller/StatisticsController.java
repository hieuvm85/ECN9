package com.example.Ecommerce_BE.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {
	
	@Autowired
	StatisticsService statisticsService;
	@Autowired
	OrderService orderService;
	
	@GetMapping("/interest")
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
	
	
	@GetMapping("/transportFee")
	public ResponseEntity<?> getTransportFee(@RequestBody StatisticsRequest timeZoneRequest) {
	    Transport transport = calculateTransportFee(timeZoneRequest);
	    transport.setStartDate(timeZoneRequest.getStartDate());
	    transport.setEndDate(timeZoneRequest.getEndDate());
	    return ResponseEntity.ok(transport);
	}
	
	
	
	@GetMapping("/export/excel/transportFee")
	public ResponseEntity<?> getExportTransportFee(@RequestBody StatisticsRequest timeZoneRequest) {
	    Transport transport = calculateTransportFee(timeZoneRequest);

	    try {
	        // Tạo workbook mới
	        Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("Báo cáo thanh toán bên vận chuyển");

	        // Tiêu đề
	        Row titleRow = sheet.createRow(0);
	        Cell titleCell = titleRow.createCell(0);
	        titleCell.setCellValue("Báo cáo thanh toán bên vận chuyển");

	        // Ngày bắt đầu và kết thúc
	        Row dateRow = sheet.createRow(1);
	        Cell startDateCell = dateRow.createCell(0);
	        startDateCell.setCellValue("Ngày bắt đầu:");
	        Cell endDateCell = dateRow.createCell(1);
	        endDateCell.setCellValue(timeZoneRequest.getStartDate().toString());
	        Cell endDateCell_1 = dateRow.createCell(2);
	        endDateCell.setCellValue("Ngày kết thúc:");
	        Cell endDateCell_2 = dateRow.createCell(3);
	        endDateCell.setCellValue(timeZoneRequest.getEndDate().toString());

	        // Dữ liệu
	        int rowNum = 3;
	        Row dataRow;
	        Cell dataCell;

	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng số đơn hàng thanh toán bằng tiền mặt:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getTotalPayCashOrder());
	        
	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng tiền mặt hàng các đơn hàng thanh toán bằng tiền mặt:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getTotalMoneyItemPayCashOrder());

	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng số đơn hàng thanh toán bằng ví điện tử:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getTotalPayWalletOrder());
	        
	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng tiền mặt hàng các đơn hàng thanh toán bằng ví điện tử:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getTotalMoneyItemPayWalletOrder());

	        // Tiếp tục với các thành phần khác...
	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng số đơn hàng hủy:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getTotalCancelOrder());

	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng tiền vận chuyển của các đơn hàng đã hủy:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getTotalMoneyShipCancelOrder());

	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng tiền vận chuyển của các đơn hàng thanh toán bằng tiền mặt:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getTotalMoneyShipPayCashOrder());

	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng tiền vận chuyển của các đơn hàng thanh toán bằng ví điện tử:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getTotalMoneyShipPayWalletOrder());

	        dataRow = sheet.createRow(rowNum++);
	        dataCell = dataRow.createCell(0);
	        dataCell.setCellValue("Tổng tiền phải thu:");
	        dataCell = dataRow.createCell(1);
	        dataCell.setCellValue(transport.getAmountReceivable());

	        // Ký tên giám đốc
	        Row directorRow = sheet.createRow(rowNum++);
	        Cell directorCell = directorRow.createCell(0);
	        directorCell.setCellValue("Chữ ký giám đốc");

	        // Ghi workbook vào ByteArrayOutputStream
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        workbook.close();

	        // Trả về file Excel cho client
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "attachment; filename=transport_fee_report.xlsx");
	        return ResponseEntity
	                .ok()
	                .headers(headers)
	                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	                .body(new ByteArrayResource(outputStream.toByteArray()));
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity
	                .badRequest()
	                .body(null);
	    }
	}
	
	///báo cáo top 10 sản phẩm bán chạy
	@GetMapping("/top/products")
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
	@GetMapping("/top/shops")
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
	 
	@GetMapping("/order")
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
