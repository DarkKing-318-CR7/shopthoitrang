// src/main/java/com/uth/shoptmdt/service/BlogService.java
package com.uth.shoptmdt.service;

import com.uth.shoptmdt.service.dto.BlogPost;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final List<BlogPost> posts = List.of(
            new BlogPost(
                    1L,
                    "8 cách mặc váy đầy cảm hứng trong mùa đông",
                    "Mùa đông không chỉ là lúc chúng ta khoác áo ấm, mà còn là cơ hội thể hiện cá tính và gu thời trang riêng...",
                    """
                    Mùa đông không chỉ là lúc chúng ta bước vào cánh cửa với những chiếc áo ấm, 
                    mà đây còn là cơ hội để các cô gái thể hiện cá tính và gu thời trang riêng của mình.
                    Trong bài viết này, shop sẽ gợi ý cho bạn 8 cách phối váy mùa đông vừa ấm vừa xinh...
                    """,
                    "blog-04.jpg",
                    LocalDate.of(2025, 11, 22),
                    "Thời trang"
            ),
            new BlogPost(
                    2L,
                    "Phối đồ nam đi sự kiện thanh lịch với áo vest sáng màu và quần jean",
                    "Áo vest sáng màu kết hợp cùng quần jean – công thức vừa trẻ trung vừa lịch sự cho nam giới...",
                    """
                    Bạn có thể phối áo vest trắng, be hoặc xám nhạt với quần jean xanh đậm/đen,
                    kèm sneaker hoặc giày da tối màu để giữ được sự cân bằng giữa thanh lịch và năng động...
                    """,
                    "blog-05.jpg",
                    LocalDate.of(2025, 11, 18),
                    "Nam tính"
            ),
            new BlogPost(
                    3L,
                    "20+ cách phối đồ du lịch mùa đông cho nam và nữ",
                    "Gợi ý outfit du lịch mùa đông ấm áp, gọn nhẹ nhưng vẫn thời trang cho cả nam và nữ...",
                    """
                    Việc chuẩn bị quần áo trước khi đi du lịch luôn tiêu tốn rất nhiều thời gian.
                    Đặc biệt là khi bạn đến những nơi có nhiệt độ thấp. 
                    Dưới đây là gợi ý các lớp quần áo, chất liệu và cách phối màu để vừa ấm vừa đẹp...
                    """,
                    "blog-06.jpg",
                    LocalDate.of(2025, 11, 16),
                    "Du lịch"
            ),
            new BlogPost(
        4L,
                "Phong cách đường phố: Outfit chất cho giới trẻ",
                "Outfit phong cách đường phố (street style) đang là xu hướng cực hot, mang đến vẻ ngoài cá tính và đầy năng lượng cho giới trẻ...",
                """
        Street Style luôn là lựa chọn hoàn hảo nếu bạn muốn thể hiện cá tính mạnh mẽ, phóng khoáng nhưng vẫn đầy thời trang.
        Hãy kết hợp hoodie dáng rộng, quần jogger, áo khoác bomber và sneaker tối màu để tạo nên tổng thể hài hòa.
        Bạn có thể phối thêm phụ kiện như mũ lưỡi trai, dây chuyền bản to hoặc balo mini để tăng điểm nhấn cho outfit.
        Đây là phong cách được rất nhiều bạn trẻ theo đuổi nhờ sự thoải mái, dễ ứng dụng và cực kỳ phù hợp để chụp ảnh street style.
        """,
                "gallery-04.jpg",
        LocalDate.of(2025, 11, 14),
        "Đường phố"
                ),new BlogPost(
                    5L,
                    "Áo khoác bomber – item không thể thiếu trong mùa lạnh",
                    "Áo khoác bomber mang lại cảm giác năng động, trẻ trung và rất dễ phối đồ — phù hợp cho cả nam và nữ...",
                    """
                    Bomber là kiểu áo khoác có thiết kế lưng bo, cổ bo và tay bo đặc trưng, giúp người mặc trông khỏe khoắn và đầy phong cách.
                    Bạn có thể kết hợp bomber với áo thun basic, quần jean slim-fit và giày sneaker để có outfit hoàn hảo trong mùa đông.
                    Với màu sắc trung tính như đen, xám, nâu hoặc xanh navy, bomber cực kỳ dễ phối và phù hợp với mọi vóc dáng.
                    Đây chắc chắn là item mà bạn không thể bỏ qua trong tủ đồ mùa lạnh.
                    """,
                    "gallery-03.jpg",
                    LocalDate.of(2025, 11, 12),
                    "Thời trang"
            )

            );

    public List<BlogPost> findAll() {
        return posts;
    }

    public Optional<BlogPost> findById(Long id) {
        return posts.stream().filter(p -> p.getId().equals(id)).findFirst();
    }
}
