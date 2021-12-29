//<script th:src="@{../js/jquery-3.6.0.min.js}"></script>
//<script>

	function writeForm(){
		location.href="/articles/insert";
	}

	$(document).ready(function () {

		/*-------------------댓글 ------------------ */
		const getAjax = function (url, no, content) {
			// resolve, reject는 자바스크립트에서 지원하는 콜백 함수이다.
			return new Promise((resolve, reject) => {
				$.ajax({
					url: url,
					method: 'POST',
					dataType: 'json',
					data: {
						no: no,
						content: content,
						articleNo: $("#article_no").val()

					},
					success: function (data) {
						resolve(data);
					},
					error: function (e) {
						reject(e);
					}
				});
			});
		}

		const removeReply = function (url, no) {
			// resolve, reject는 자바스크립트에서 지원하는 콜백 함수이다.
			return new Promise((resolve, reject) => {
				$.ajax({
					url: url,
					method: 'GET',
					dataType: 'json',
					data: {
						no: no,
						articleNo: $("#article_no").val()
					},
					success: function (data) {
						resolve(data);
					},
					error: function (e) {
						console.trace();
						reject(e);
					}
				});
			});
		}

		async function requestProcess(url, no, content) {
			try {
				console.log("rP 접속");
				let replyList = null;
				if (content == null || content == '' || typeof content == "undefined") {
					replyList = await removeReply(url, no);
				} else {
					console.log("GetAjAx 전");
					replyList = await getAjax(url, no, content);
					console.log("GetAjAx 후");
				}

				$('#replys').html("");
				let htmlStr = [];

				console.log(htmlStr);
				for (let i = 0; i < replyList.length; i++) {
					htmlStr.push('<div class="reply_list">');
					htmlStr.push('<table id=' + replyList[i].no + '>');
					htmlStr.push('<tbody>');
					htmlStr.push('<tr>');
					htmlStr.push('<td class="Content">' + replyList[i].content + "</td>");
					htmlStr.push('<tr>');
					htmlStr.push('<td>' + replyList[i].nickname + "</td>");
					htmlStr.push('</tr>');
					htmlStr.push('<tr>');
					htmlStr.push('<td>' + replyList[i].writeDate + "</td>");
					htmlStr.push('</tr>');
					htmlStr.push('</tbody>');
					htmlStr.push('<tfoot>');
					if (replyList[i].memberNo == $('#login_no').val()) {
						htmlStr.push("<tr>");
						htmlStr.push('<td><input type="button" class="modify_Reply_Form_Btn" value="수정" />&nbsp;');
						htmlStr.push('<input type="button" class="remove_Reply_Btn" value="삭제" /></td>');
						htmlStr.push('</tr>');
					}
					htmlStr.push('</tfoot>');
					htmlStr.push('</table>');
					htmlStr.push('</div>');
				}
				console.log(htmlStr);
				$('#replys').html(htmlStr.join(""));

			} catch (error) {
				console.trace();
				console.log("error : ", error);
			}
		}

		console.log();

		//댓글 작성
		$('#write_reply_btn').on('click', function () {
			const articleNo = $("#article_no").val();
			const content = $('#write_content').val();
			console.log('ajax전');
			requestProcess('/insertReply', articleNo, content);
			$("#write_content").val(""); // textarea 비우기
			console.log('ajax후');
		});


		//댓글 수정폼
		$(document).on('click', '.modify_Reply_Form_Btn', function () {
			const no = $(this).parents('table').attr('id');
			$('#modify_reply_form').insertAfter('#' + no);
			const content = $('#' + no).find('.Content').text();
			console.log('content:', content);
			$('#reply_content[placeholder]').val(content);
			$('#no').val(no);
			$('#modify_reply_form').show();
			$('#' + no).hide();
		});


		//댓글 수정폼 닫기
		$('.modify_cancle').on('click', function () {
			const no = $('#no').val();
			console.log('cancelModifyFormNo:', no);
			$('#' + no).show();
			$('#modify_reply_form').hide();
			$('#modify_reply_form').insertAfter('#writeReplyForm');
		});

		//댓글 수정
		$('.modify_reply_btn').on('click', function () {
			// 댓글No
			const no = $('#no').val();
			console.log('modifyActionNo:', no);
			const content = $('#' + no).parent('div').find('#reply_content').val();
			console.log('modifyReplyContent:', content);
			requestProcess('/modifyReply', no, content);
			$('#modify_reply_form').insertAfter('#writeReplyForm');
			$('#modify_reply_form').hide();
			$('#modify_reply_form').html();
		});


		//댓글 삭제
		$(document).on('click', '.remove_Reply_Btn', function () {
			const no = $(this).parents('table').attr('id');
			console.log('remove', no)
			requestProcess('/removeReply', no);
		});



		$(document).on('click', '#add_like_btn', function () {
			$.ajax({
				url: '/addLike',
				method: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				data: JSON.stringify({
					"articleNo": $("#article_no").val()
				}),
				success: function (data) {
					let count = data.totalcount; // 총 추천 수
					//let count = JSON.parse(data).totalCount;
					// alert(count);
					let likecount = [];
					likecount += "<button type='button' id='del_like_btn'><i class='fas fa-heart'></i> </button>";
					likecount += "<strong>" + count + "</strong>";
					$('#like_area').html("");
					$('#like_area').html(likecount);
				},
				error: function (e) {
					console.trace();
					reject(e);
				}
			});
		});


		$(document).on('click', '#del_like_btn', function () {
			$.ajax({
				url: "/delLike",
				method: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				data: JSON.stringify({
					"articleNo": $("#article_no").val()
				}),
				success: function (data) {
					let count = data.totalcount; // 총 추천 수
					let likecount = [];
					likecount += "<button type='button' id='add_like_btn'><i class='far fa-heart'></i></button>";
					likecount += "<strong>" + count + "</strong>";
					$('#like_area').html("");
					$('#like_area').html(likecount);
				},
				error: function (e) {
					console.trace();
					reject(e);
				}
			});
		})


	});
//</script>