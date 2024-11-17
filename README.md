# Voltic Store

Voltic Store is a dynamic and user-friendly e-commerce platform built using **Spring Boot** for the backend and **pure JavaScript** for the frontend. This project demonstrates a robust implementation of modern e-commerce functionalities with clean code and modular architecture.

## Features

- **User Management**: 
  - User registration and authentication (sign up, login, and logout).
  - Role-based access control (admin and user).
  
- **Product Management**:
  - Add, edit, and delete products (admin functionality).
  - Display product catalog with filters and search.

- **Shopping Cart**:
  - Add to cart, update quantities, and remove items.
  - Real-time cart updates.

- **Order Management**:
  - Place orders with a secure checkout process.
  - View order history.

- **Responsive Design**:
  - Fully responsive UI for a seamless experience across devices.

## Tech Stack

### Backend:
- **Spring Boot**:
  - RESTful APIs for handling product, cart, and user data.
  - Spring Security for authentication and authorization.
  - JPA and Hibernate for database interactions.

- **Database**:
  - MySQL (or PostgreSQL) for data persistence.

### Frontend:
- **Pure JavaScript**:
  - Dynamic rendering and interactivity using vanilla JS.
  - DOM manipulation for seamless user experience.
  - Fetch API for communication with backend APIs.

- **HTML5 & CSS3**:
  - Clean and modern design.
  - Responsive layout.

## Setup & Installation

### Prerequisites:
- Java 17+
- Maven
- Node.js (optional, for testing JavaScript with a local server)
- MySQL

### Steps:

1. **Clone the repository**:
   ```
   git clone https://github.com/your-username/voltic-store.git
   cd voltic-store
   ```

2. **Backend Setup**:
   - Update `application.properties` with your database credentials.
   - Build the project:
     ```
     mvn clean install
     ```
   - Run the Spring Boot application:
     ```
     mvn spring-boot:run
     ```

3. **Frontend Setup**:
   - Open the `index.html` file in your browser to test the frontend.

4. **Run Locally**:
   - Backend runs at `http://localhost:8080`.
   - Ensure the frontend fetches data from this endpoint.

## API Endpoints

### Authentication
- `POST /api/auth/register`: Register a new user.
- `POST /api/auth/login`: Authenticate user and return a token.

### Products
- `GET /api/products`: Fetch all products.
- `POST /api/products`: Add a new product (Admin only).
- `PUT /api/products/{id}`: Update product details (Admin only).
- `DELETE /api/products/{id}`: Delete a product (Admin only).

### Cart
- `GET /api/cart`: Get user cart items.
- `POST /api/cart`: Add an item to the cart.
- `PUT /api/cart/{id}`: Update cart item quantity.
- `DELETE /api/cart/{id}`: Remove an item from the cart.

### Orders
- `POST /api/orders`: Place an order.
- `GET /api/orders`: Fetch user orders.

## Screenshots

![Home Page](https://via.placeholder.com/800x400)
![Product Page](https://via.placeholder.com/800x400)
![Cart Page](https://via.placeholder.com/800x400)

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For any questions or feedback, please contact us at [support@volticstore.com](mailto:support@volticstore.com).
