$(document).ready(function() {
    // Nên để PAGE_SIZE ở một nơi và đồng bộ với backend
    const PAGE_SIZE = 10;

    /**
     * Hàm gọi AJAX chung để lấy bài viết.
     * @param {number} page - Số trang cần tải.
     * @param {string} keyword - Từ khóa tìm kiếm.
     * @param {boolean} isSearch - True nếu đây là một lần tìm kiếm mới, False nếu là "Tải thêm".
     */
    function fetchPosts(page, keyword, isSearch) {
        const loadMoreBtn = $('#loadMoreBtn');
        const postContainer = $('#post-list-container');

        // Hiển thị trạng thái "đang tải" cho người dùng
        if (isSearch) {
            postContainer.html('<p style="text-align: center;">Đang tìm kiếm...</p>');
        } else {
            // Khi tải thêm, chỉ thay đổi nút
            loadMoreBtn.text('Đang tải...').prop('disabled', true);
        }

        $.ajax({
            url: '/api/posts',
            type: 'GET',
            data: {
                page: page,
                size: PAGE_SIZE,
                sort: 'id,desc', // Luôn sắp xếp bài mới nhất lên đầu
                keyword: keyword
            },
            success: function(fragment) {
                if (isSearch) {
                    // Nếu là tìm kiếm mới, thay thế toàn bộ nội dung container
                    postContainer.html(fragment);
                } else {
                    // Nếu là "Tải thêm", xóa nút "Tải thêm" cũ...
                    loadMoreBtn.parent().remove();
                    // ...và nối fragment mới (chứa cả bài viết và nút "Tải thêm" mới nếu có) vào cuối
                    postContainer.append(fragment);
                }
            },
            error: function() {
                alert('Có lỗi xảy ra, vui lòng thử lại.');
                // Khôi phục lại nút nếu có lỗi khi tải thêm
                if (!isSearch) {
                    loadMoreBtn.text('Tải thêm').prop('disabled', false);
                }
            }
        });
    }

    // Sự kiện submit form tìm kiếm
    $('#searchForm').on('submit', function(e) {
        e.preventDefault(); // Ngăn form tải lại trang
        const keyword = $('#searchInput').val();
        // Khi tìm kiếm, luôn bắt đầu từ trang 0
        fetchPosts(0, keyword, true);
    });

    // Sự kiện click nút "Tải thêm"
    // Sử dụng event delegation vì nút #loadMoreBtn bị xóa và tạo lại liên tục
    $('#post-list-container').on('click', '#loadMoreBtn', function() {
        const button = $(this);
        const nextPage = button.data('next-page');
        const keyword = button.data('keyword'); // Lấy từ khóa đang tìm kiếm (nếu có)
        fetchPosts(nextPage, keyword, false);
    });
});