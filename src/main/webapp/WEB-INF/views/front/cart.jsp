<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<%@ include file="common-base.jsp"%>
	<link href="css/cart.css" rel="stylesheet" type="text/css" />
	
</head>
<body>
	<%@ include file="common-header.jsp"%>
	<div class="container cart">
			<div class="span24">
				<div class="step step1" style="color: green;font-size: 20pt;font-weight: bold;">
				购物车信息
				</div>
				<div class="step step1" style="color: red;font-size: 20pt;font-weight: bold;">
				(提交订单：请加入商品多选功能，把选择的商品提交订单)
				</div>
				<table>
					<tbody>
						<tr>
							<th>图片</th>
							<th>商品</th>
							<th>价格</th>
							<th>数量</th>
							<th>小计</th>
							<th>操作</th>
							<th>结算</th>
						</tr>
							<tr>
								<td width="60"><img src="products/${cp.pimg}" /></td>
								<td><a target="_blank">${cp.pname}</a></td>
								<td name='pprice' value='${cp.pprice}'>￥ ${cp.pprice}元</td>
								<td class="quantity" width="60">
								<input  value="${cp.pcount}" name='pcount' onchange='changeCart(${cp.pid},${cp.pcount},this);' />
								</td>
								<td width="140"><span class="subtotal">￥${cp.pprice*cp.pcount}元</span></td>
								<td><a href="javascript:void(0)" onclick="deleteCart(${cp.pid});" class="delete">删除</a></td>
								<td>
									<input type="checkbox" checked="checked" 
									value="${cp.pid}" name="buyPids" 
									onchange="countPrice();">
								</td>
							</tr>
					</tbody>
				</table>
				<dl id="giftItems" class="hidden" style="display: none;">
				</dl>
				<div class="total" >
					<em style="font-size: 16pt;font-weight: bold;"> 【已经选中商品的下单统计】 </em> 
					商品类别数量: <strong id="countProCate">${countProCate}类</strong>;
					商品数量: <strong id="countPro" >${countPro}个</strong>;
					商品总价: <strong id="sumPrice">${sumPrice}元</strong>
				</div>
				<div class="bottom">
						<a href="####"  class="clear">清空购物车</a> 
					
						<a href="javascript:void(0)" onclick="commitCart()" class="submit">结算购物车</a>
				</div>

			</div>
			<div class="span24">
				<div class="step step1">
					<span> <h2>亲!您还没有购物!请先去购物!</h2></span>
				</div>
			</div>
	</div>
	
	<%@ include file="common-footer.jsp"%>
</body>
</html>