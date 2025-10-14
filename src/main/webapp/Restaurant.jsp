<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.util.List,com.tap.model.Restaurant" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>FoodieExpress</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="sstyle.css" />
</head>
<body>
 <nav class="header">
    <h1>BiteBuddy</h1>
    <ul>
      <li><a href="#home">Home</a></li>
      <li><a href="#about">About</a></li>
      <li><a href="cart.jsp">Cart</a></li>
      <li><a href="login.html">Login</a></li>
      <li><a href="allOrders.jsp">Orders</a></li>
      <!--<li><a href="logout.jsp">Logout</a></li>-->
    </ul>
  </nav>


  
  <section id="home" class="hero">
  <video autoplay muted loop class="background-video">
    <source src="https://videos.pexels.com/video-files/3298718/3298718-uhd_2732_1440_25fps.mp4" type="video/mp4" />
    Your browser does not support the video tag.
  </video>
  <div class="hero-content">
    <h2>Welcome to BiteBuddy 🍴</h2>
    <p>"Food that understands your stack overflow — Welcome to BiteBuddy" 🍲💾</p>
  </div>
</section>

  
  	<section id="restaurants" class="restaurants">
	
	<%
	List<Restaurant> restaurants=(List<Restaurant>) request.getAttribute("allResturants");
	if(restaurants !=null && !restaurants.isEmpty()){
	for(Restaurant restaurant : restaurants){%>
		<a href="menu?restaurant_id=<%= restaurant.getRestaurant_id()%>">

    <div class="card">
      <img src="<%=restaurant.getImagepath() %>" alt="Spice Hub">
      <h3><%=restaurant.getName() %></h3>
      <p><strong>ETA:</strong> <%=restaurant.getDelivery_time() %></p>
      <p><strong>Address:</strong> <%=restaurant.getAddress() %></p>
      <p><strong>Cuisine:</strong> <%=restaurant.getCuisine_type() %></p>
    </div>
 
		</a>
	
		<% }
	}
%>
 </section>
<!-- About Section -->
<section id="about" class="about-section">
  <h2>About BiteBuddy</h2>
  <p>BiteBuddy connects hungry hearts with their favorite restaurants. With fast delivery, a variety of cuisines, and easy ordering, we're here to serve you joy on a plate.</p>
</section>

  <footer id="contact">
    <p>&copy; 2025 BiteBuddy. All rights reserved.</p>
  </footer>
    
</body>
</html>