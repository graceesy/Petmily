<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
	<jsp:include page="header.jsp" />
     <!-- Product section-->
        <section class="py-5" style="height=700;">
            <div class="container px-4 px-lg-5 my-5">
                <div class="row gx-4 gx-lg-5 align-items-center">
                    <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src="${path}/resource/images/002.jpg" alt="..." /></div>
                    <div class="col-md-6">
                        <div class="small mb-1">Premium Pakage</div>
                        <h1 class="display-5 fw-bolder">프리미엄 패키지</h1>
                        <div class="fs-5 mb-5">
                            <span class="text-decoration-line-through">39,800 (원)</span>
                        </div>
                        <p class="lead">
						 <span>첫번째상품 :</span><span style="color:black"> 펫밀리 특제고급사료 2kg</span> <br>
                  		 <span>두번째상품 :</span><span style="color:black"> 카리에스 덴탈껌 </span> <br> 
                  	 	 <span>세번째상품 :</span><span style="color:black"> 프레쉬 덴탈 츄</span> <br> 
						</p>
                        <div class="d-flex">
                            <button class="btn btn-outline-dark flex-shrink-0" style="margin-left: 322px;" id="gold">
                                <i class="bi-cart-fill me-1"></i>
                                구독 하기
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </section>


	<jsp:include page="footer.jsp" />
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>	
	<script>
$(document).ready(function() {
	  $('li.active').removeClass('active');
	  $('a[href="' + "/subscribe" + '"]').closest('li').addClass('active');
	  
	  $(function () {
			$("#gold").click(function() {
				$.ajax({
					type:"post",
					dataType:"text",
					async:false,
					url:"http://localhost:8080/mem_gold",
					data:{},
					success: function(data, textStatus) {
						if(data=="true") {
							Swal.fire({
					            icon: 'error',
					            title: '이미 구독중인 상품입니다.',
					            text: '돌아가주세요.',
					          }).then(function(){
		                          location.href="http://localhost:8080/subscribe";
		                      });
						}else {
		                          location.href="http://localhost:8080/mem_pay";
						}
					}
				});        		
			});
		})	  
	});
</script>
	