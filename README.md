# Dự án Java Spring Boot cuối kì - Website thương mại điện tử


## I. Software development Principles, Patterns and Best Practices

### Principles
1. DRY (Don't Repeat Yourself) + Tái sử dụng thành phần (Component Reuse)
    - Tận dụng tái sử dụng thành phần để tránh việc lặp lại code không cần thiết.
    - Giảm thiểu việc viết lại code để tăng tính bảo trì, sửa chữa và mở rộng của dự án.

2. SoC (Separation of Concerns)
    - Phân chia các thành phần có tính chất khác nhau vào các đoạn code riêng biệt.
    - Duy trì sự rõ ràng và dễ đọc của mã nguồn.
    - Dễ dàng bảo trì và mở rộng từng phần riêng lẻ.

### Patterns
1. Mô hình MVC (Model-View-Controller)
    - Đại diện cho dữ liệu và logic liên quan đến dữ liệu (Model).
    - Đại diện cho giao diện người dùng và hiển thị thông tin (View).
    - Đại diện cho logic xử lý yêu cầu và điều phối hoạt động (Controller).

2. Repository Pattern (JPA)
    - Sử dụng Repository Pattern với JPA (Java Persistence API) để quản lý truy cập cơ sở dữ liệu.
    - Tách biệt logic xử lý dữ liệu và truy vấn cơ sở dữ liệu.
    - Tăng tính tái sử dụng và dễ dàng thay đổi cơ sở dữ liệu.

### Best Practices
1. Tổ chức code theo dạng module
    - Tổ chức code thành các module nhỏ, mỗi module chịu trách nhiệm cho một chức năng cụ thể.
    - Duy trì sự rõ ràng và dễ quản lý của mã nguồn.

2. Xử lý lỗi (Error handling)
    - Thực hiện cơ chế xử lý lỗi và exception phù hợp.
    - Sử dụng try-catch, throw exception và xử lý các trường hợp lỗi một cách tổ chức và thông minh.
    - Đảm bảo tính ổn định và đáng tin cậy của ứng dụng.

## II. Giải thích code ngắn gọn
Web Java Spring Boot thương mại điện tử, bán các sản phẩm liên quan đến công nghệ như điện thoại, laptop, phụ kiện...
<!-- 
Sơ đồ UML:
![UML](./snapshots/diagram.png) -->

Project chia làm các thư mục chính:
### Repository
Repository là thành phần chịu trách nhiệm làm việc với cơ sở dữ liệu. Trong đoạn code, nó được mở rộng từ JPA (Java Persistence API) để cung cấp các phương thức để truy vấn và tương tác với cơ sở dữ liệu.

### Service

Service là thành phần chịu trách nhiệm xử lý logic liên quan đến dữ liệu. Nó sử dụng các phương thức từ Repository để thực hiện các thao tác trên cơ sở dữ liệu và trả về các kết quả đã được xử lý.

### Controller

Controller là thành phần xử lý các yêu cầu từ phía người dùng và quản lý luồng điều khiển trong ứng dụng. Nó nhận thông tin từ giao diện người dùng và sử dụng các phương thức từ Service để lấy dữ liệu hoặc thực hiện các thao tác cần thiết. Sau đó, nó trả về dữ liệu hoặc chuyển hướng đến các trang tương ứng.

### Model

Model là thành phần đại diện cho các dữ liệu trong cơ sở dữ liệu. Nó được sử dụng để lưu trữ và truyền dữ liệu giữa các thành phần khác nhau trong ứng dụng.

### DTO

DTO (Data Transfer Object) là các đối tượng ngắn gọn hơn của Model, thường được sử dụng để truyền dữ liệu qua API, làm giảm thông tin không cần thiết và tăng hiệu suất truyền dữ liệu.

### Specification
Specification là thành phần cho phép tùy chỉnh các câu truy vấn trong Repository để lấy dữ liệu theo các tiêu chí cụ thể, giúp tăng khả năng linh hoạt trong việc truy vấn cơ sở dữ liệu.

### Resource
Resource chứa các tài nguyên phía giao diện như file CSS, JavaScript và các template HTML. 

### Config
Chứa file Setup security, phân riêng các rold cho người chưa đăng nhập, người đã đăng nhập và admin.


## Các bước để chạy dự án trên Localhost
1. Tạo database: (Yêu cầu có sẵn phần mềm docker)
    - Mở cmd trong thư mục của dự án, gõ docker-compose up -d để khởi tạo database bằng docker.
    - Như vậy là xong phần database. Data sẽ tự được thêm bằng code khi khởi chạy project.
2. Setup file application.properties.
   - Sử dụng luôn code đã tạo, hoặc có thể thay đổi 3 dòng code sau đây trong file application.properties với dữ liệu phù hợp trong localhost:
      - spring.datasource.url=jdbc:mysql://localhost:5432/springcommerce
        spring.datasource.username=<username để vào được database>
        spring.datasource.password=<password>

3. Chạy project trong intellij.
4. Truy cập http://localhost:8080/ để vào web (hoặc đổi 8081 thành cổng mà bạn đặt trong application.properties).
    1. Khi chưa đăng nhập, người dùng có thể:
        1. Vào trang chủ.
        2. Đăng nhập - Đăng kí
        2. Vào Shop để xem sản phẩm.
        3. Search sản phẩm.
        4. Filter sản phẩm bằng thanh filter bên trái màn hình: thể loại, mức giá, màu sắc, kích cỡ. Có thể áp dụng nhiều thuộc tính filter cùng lúc.
        5. Click vào sản phẩm bất kì để xem chi tiết sản phẩm.
        6. Nếu người dùng click "thêm vào giỏ hàng", hoặc cố ý nhập vào đường link /cart hoặc /order thì chuyển đến trang đăng nhập.
    2. Khi đã đăng nhập:
        1. Thêm sản phẩm vào giỏ hàng.
        2. Thay đổi giỏ hàng: đổi số lượng, xoá bớt sản phẩm trong giỏ.
        3. Xác nhận đặt hàng.
        4. Xem lịch sử mua hàng.
        5. Huỷ đơn đã đặt.
        6. Đăng xuất
        7. Thêm vào wishlish
        8. Đổi mật khẩu.
    3. Với tài khoản Admin:
        1. Khi đăng nhập sẽ chuyển thẳng vào các trang admin.
        2. Admin có thể thay đổi data của Product, User, Category và Brand. 
        3. Có thể tìm kiếm Product, Category và Brand bằng tên để tiện cho việc thay đổi; có thể tìm kiếm người dùng bằng email.
        4. Khi quay về trang giao diện shop, với riêng các tài khoản admin sẽ có thêm link đến thẳng trang admin. Điều này để thuận tiện cho việc admin vừa có thể thay đổi data, vừa có thể xem kết quả thay đổi trên các trang thông thường.