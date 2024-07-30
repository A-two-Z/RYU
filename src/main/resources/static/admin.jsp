<!DOCTYPE html>
<html lang="ko">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>주문 전송</title>
	<style>
		button:disabled {
			background-color: #ddd;
			color: #666;
			cursor: not-allowed;
		}

		button {
			background-color: #4CAF50;
			color: white;
			border: none;
			padding: 10px 20px;
			text-align: center;
			text-decoration: none;
			display: inline-block;
			font-size: 16px;
			margin: 4px 2px;
			cursor: pointer;
		}

		table tr td {
			padding: 10px 30px;
		}
	</style>
</head>

<body>
	<h1>관리자 페이지</h1>
	<table>
		<thead>
			<tr>
				<td>주문 인덱스</td>
				<td>주문 번호</td>
				<td>전송 버튼</td>
			</tr>
		</thead>
		<tbody>
			<tr id="order-row">
				<td>2</td>
				<td id="order-number">20240725151303</td>
				<td><button id="order-button" onclick="sendOrder()" disabled>전송 완료</button></td>
			</tr>
			<tr id="order-row">
				<td>1</td>
				<td id="order-number">20240725102536</td>
				<td><button id="order-button" onclick="sendOrder()" disabled>전송 완료</button></td>
			</tr>
			<tr id="order-row">
				<td>1</td>
				<td id="order-number">20240725102536</td>
				<td><button id="order-button" onclick="sendOrder()" disabled>전송 완료</button></td>
			</tr>
			<tr id="order-row">
				<td>1</td>
				<td id="order-number">20240725102536</td>
				<td><button id="order-button" onclick="sendOrder()" disabled>전송 완료</button></td>
			</tr>
			<tr id="order-row">
				<td>1</td>
				<td id="order-number">20240725102536</td>
				<td><button id="order-button" onclick="sendOrder()" disabled>전송 완료</button></td>
			</tr>
			<tr id="order-row">
				<td>1</td>
				<td id="order-number">20240725102536</td>
				<td><button id="order-button" onclick="sendOrder()" disabled>전송 완료</button></td>
			</tr>
		</tbody>
	</table>

	<script>
		function sendOrder() {
			const orderNumber = document.getElementById('order-number').innerText;
			const orderButton = document.getElementById('order-button');

			fetch('http://localhost:8080/MulToRobot/orderToQ', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({'orderNumber': orderNumber})
			})
				.then(response => response.json())
				.then(data => {
					// Check the response status
					if (data.orderStatus === 1) {
						// Disable the button and change the text
						orderButton.disabled = true;
						orderButton.innerText = '전송 완료';
					} else {
						// Handle other statuses if necessary
						alert('주문 전송에 실패했습니다.');
					}
				})
				.catch(error => {
					console.error('Error:', error);
					alert('주문 전송에 실패했습니다.');
				});
		}


	</script>
</body>

</html>