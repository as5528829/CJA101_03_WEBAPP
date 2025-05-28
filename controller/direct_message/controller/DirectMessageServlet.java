package com.foodtimetest.direct_message.controller;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.foodtimetest.direct_message.*;
	@WebServlet(name = "directMessageServlet", urlPatterns = {"/directmessage/dm.do"})
public class DirectMessageServlet extends HttpServlet{

		public void doGet(HttpServletRequest req, HttpServletResponse res)
				throws ServletException, IOException {
			doPost(req, res);
		}

		public void doPost(HttpServletRequest req, HttpServletResponse res)
				throws ServletException, IOException {

			req.setCharacterEncoding("UTF-8");
			String action = req.getParameter("action");
			
			
			if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

				List<String> errorMsgs = new LinkedList<String>();
				// Store this set in the request scope, in case we need to
				// send the ErrorPage view.
				req.setAttribute("errorMsgs", errorMsgs);

					/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
					String str = req.getParameter("dmId");
					if (str == null || (str.trim()).length() == 0) {
						errorMsgs.add("請輸入訊息編號");
					}
					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						RequestDispatcher failureView = req
								.getRequestDispatcher("/directmessage/select_page.jsp");
						failureView.forward(req, res);
						return;//程式中斷
					}
					
					Integer dmId = null;
					try {
						dmId = Integer.valueOf(str);
					} catch (Exception e) {
						errorMsgs.add("訊息編號格式不正確");
					}
					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						RequestDispatcher failureView = req
								.getRequestDispatcher("/directmessage/select_page.jsp");
						failureView.forward(req, res);
						return;//程式中斷
					}
					
					/***************************2.開始查詢資料*****************************************/
					DirectMessageService dmSvc = new DirectMessageService();
					DirectMessageVO dmVO = dmSvc.getOneDm(dmId);
					if (dmVO == null) {
						errorMsgs.add("查無資料");
					}
					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						RequestDispatcher failureView = req
								.getRequestDispatcher("/directmessage/select_page.jsp");
						failureView.forward(req, res);
						return;//程式中斷
					}
					
					/***************************3.查詢完成,準備轉交(Send the Success view)*************/
					req.setAttribute("dmVO", dmVO); 
					String url = "/directmessage/listOneDm.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); 
					successView.forward(req, res);
			}
			
			
			if ("getOne_For_Update".equals(action)) { // 來自listAllEmp.jsp的請求

				List<String> errorMsgs = new LinkedList<String>();
				// Store this set in the request scope, in case we need to
				// send the ErrorPage view.
				req.setAttribute("errorMsgs", errorMsgs);
				
					/***************************1.接收請求參數****************************************/
					Integer dmId = Integer.valueOf(req.getParameter("dmId"));
					
					/***************************2.開始查詢資料****************************************/
					DirectMessageService dmSvc = new DirectMessageService();
					DirectMessageVO dmVO = dmSvc.getOneDm(dmId);
									
					/***************************3.查詢完成,準備轉交(Send the Success view)************/
					req.setAttribute("dmVO", dmVO);         // 資料庫取出的empVO物件,存入req
					String url = "/directmessage/update_dm_input.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_cart_input.jsp
					successView.forward(req, res);
			}
			
			
			if ("update".equals(action)) { // 來自update_emp_input.jsp的請求
				
				List<String> errorMsgs = new LinkedList<String>();
				// Store this set in the request scope, in case we need to
				// send the ErrorPage view.
				req.setAttribute("errorMsgs", errorMsgs);
			
					/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				Integer dmId = validateIntegerParameter(req, "dmId", errorMsgs, "訊息編號格式不正確");
				Integer memId = validateIntegerParameter(req, "memId", errorMsgs, "會員編號格式不正確");
	            Integer smgrId = validateIntegerParameter(req, "smgrId", errorMsgs, "管理員編號格式不正確");
	        	String messContent = req.getParameter("messContent");
				if (messContent == null || messContent.trim().length() == 0) {
					errorMsgs.add("訊息內容: 請勿空白");
				}
//				String messTime = req.getParameter("messTime");
				Integer messDirection = Integer.valueOf(req.getParameter("messDirection"));
				
	            DirectMessageVO dmVO = new DirectMessageVO();
					dmVO.setDmId(dmId);
					dmVO.setMemId(memId);
					dmVO.setSmgrId(smgrId);
					dmVO.setMessContent(messContent);	
//					dmVO.setMessTime(messTime);
					dmVO.setMessDirection(messDirection);  
		
					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						req.setAttribute("dmVO", dmVO); // 含有輸入格式錯誤的empVO物件,也存入req
						RequestDispatcher failureView = req
								.getRequestDispatcher("/directmessage/update_dm_input.jsp");
						failureView.forward(req, res);
						return; //程式中斷
					}
					
					/***************************2.開始修改資料*****************************************/
					DirectMessageService dmSvc = new DirectMessageService();
					dmVO = dmSvc.updateDm(dmId,memId,smgrId,messContent,messDirection);
					
					/***************************3.修改完成,準備轉交(Send the Success view)*************/
					req.setAttribute("dmVO", dmVO); // 資料庫update成功後,正確的的empVO物件,存入req
					String url = "/directmessage/listOneDm.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
					successView.forward(req, res);
			}

	        if ("insert".equals(action)) { // 來自addSmg.jsp的請求  
				
				List<String> errorMsgs = new LinkedList<String>();
				// Store this set in the request scope, in case we need to
				// send the ErrorPage view.
				req.setAttribute("errorMsgs", errorMsgs);

					/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				
//				Integer dmId = validateIntegerParameter(req, "dmId", errorMsgs, "訊息編號格式不正確");
				Integer memId = validateIntegerParameter(req, "memId", errorMsgs, "會員編號格式不正確");
	            Integer smgrId = validateIntegerParameter(req, "smgrId", errorMsgs, "管理員編號格式不正確");
	        	String messContent = req.getParameter("messContent");
				if (messContent == null || messContent.trim().length() == 0) {
					errorMsgs.add("訊息內容: 請勿空白");
				}
				java.sql.Timestamp messTime = new java.sql.Timestamp(System.currentTimeMillis());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String formattedNow = formatter.format(messTime);
				
				Integer messDirection = Integer.valueOf(req.getParameter("messDirection"));
				DirectMessageVO dmVO = new DirectMessageVO();
//				dmVO.setDmId(dmId);
				dmVO.setMemId(memId);
				dmVO.setSmgrId(smgrId);
				dmVO.setMessContent(messContent);	
				dmVO.setMessTime(messTime);
				dmVO.setMessDirection(messDirection);  
	

					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						req.setAttribute("dmVO", dmVO); // 含有輸入格式錯誤的empVO物件,也存入req
						RequestDispatcher failureView = req
								.getRequestDispatcher("/directmessage/addDm.jsp");
						failureView.forward(req, res);
						return;
					}
					
					/***************************2.開始新增資料***************************************/
					DirectMessageService dmSvc = new DirectMessageService();
					dmVO = dmSvc.addDm(memId,smgrId,messContent,messTime,messDirection);
					
					/***************************3.新增完成,準備轉交(Send the Success view)***********/
					String url = "/directmessage/listAllDm.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); 
					successView.forward(req, res);				
			}
			
			
			if ("delete".equals(action)) { // 來自listAllEmp.jsp
	
				List<String> errorMsgs = new LinkedList<String>();
				// Store this set in the request scope, in case we need to
				// send the ErrorPage view.
				req.setAttribute("errorMsgs", errorMsgs);
		
					/***************************1.接收請求參數***************************************/
					Integer dmId = Integer.valueOf(req.getParameter("dmId"));
					
					/***************************2.開始刪除資料***************************************/
					DirectMessageService dmSvc = new DirectMessageService();
					dmSvc.delete(dmId);
					
					/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
					String url = "/directmessage/listAllDm.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
					successView.forward(req, res);
			}
		}

		   private Integer validateIntegerParameter(HttpServletRequest req, String paramName, 
                   List<String> errorMsgs, String errorMessage) {
			   try {
				   String paramValue = req.getParameter(paramName);
				   if (paramValue == null || paramValue.trim().isEmpty()) {
					   errorMsgs.add(paramName + "不能為空");
					   return null;
				   }
				   return Integer.valueOf(paramValue.trim());
			   		} catch (NumberFormatException e) {
				   errorMsgs.add(errorMessage);
				   return null;
			   		}
		   		}
	}

