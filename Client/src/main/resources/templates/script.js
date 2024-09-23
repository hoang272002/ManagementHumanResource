function showSection(sectionId) {
    // Ẩn tất cả các phần
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
      section.style.display = 'none';
      section.classList.remove('active');
    });
  
    // Hiện phần được chọn với hiệu ứng chuyển đổi
    const selectedSection = document.getElementById(sectionId);
    selectedSection.style.display = 'block';
    setTimeout(() => {
      selectedSection.classList.add('active');
    }, 10);
  }

  document.getElementById('point-gift-form').addEventListener('submit', function(event) {
    event.preventDefault();
  
    // Lấy giá trị từ form
    const employee = document.getElementById('employee-select').value;
    const points = document.getElementById('points').value;
  
    // Giả sử chúng ta sẽ gửi yêu cầu tới server để cập nhật điểm cho nhân viên
    // Đoạn mã sau chỉ là ví dụ về xử lý sự kiện và phản hồi
  
    // Giả lập cập nhật điểm và phản hồi
    document.getElementById('gift-response').innerHTML = `Bạn đã tặng ${points} điểm cho nhân viên ${employee.replace('_', ' ')}`;

  
    // Reset form sau khi tặng điểm
    document.getElementById('point-gift-form').reset();
  });
  
  function editEmployee(button) {
    // Lấy hàng chứa thông tin nhân viên
    const row = button.parentElement.parentElement;
    
    // Lấy các giá trị từ hàng đó
    const name = row.children[0].innerText;
    const id = row.children[1].innerText;
    const tax = row.children[2].innerText;
    const address = row.children[3].innerText;
    const phone = row.children[4].innerText;
    const bank = row.children[5].innerText;
    
    // Hiển thị form chỉnh sửa và điền thông tin nhân viên
    document.getElementById('edit-form-section').style.display = 'block';
    document.getElementById('employee-name').value = name;
    document.getElementById('employee-id').value = id;
    document.getElementById('employee-tax').value = tax;
    document.getElementById('employee-address').value = address;
    document.getElementById('employee-phone').value = phone;
    document.getElementById('employee-bank').value = bank;
  }
  
  // Giả lập xử lý cập nhật thông tin nhân viên
  document.getElementById('edit-employee-form').addEventListener('submit', function(event) {
    event.preventDefault();
    
    // Lấy giá trị từ form
    const name = document.getElementById('employee-name').value;
    const id = document.getElementById('employee-id').value;
    const tax = document.getElementById('employee-tax').value;
    const address = document.getElementById('employee-address').value;
    const phone = document.getElementById('employee-phone').value;
    const bank = document.getElementById('employee-bank').value;
    
    // Giả lập cập nhật thông tin nhân viên trên bảng
    alert('Thông tin nhân viên ${name} đã được cập nhật!');
  
    // Ẩn form sau khi cập nhật
    document.getElementById('edit-form-section').style.display = 'none';
  })