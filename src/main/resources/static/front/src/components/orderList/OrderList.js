import React, { useState, useEffect } from 'react';
import '../../assets/OrderList.scss';

function OrderList() {
    const [orders, setOrders ] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [orderStatus, setOrderStatus] = useState('WAIT');

    useEffect(() => {
        fetch('http://localhost:8080/order_Num')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setOrders(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error);
                setLoading(false);
            });
    }, []);

    console.log(orders);
    const handleClick = () => {
        if (orderStatus === 'WAIT') {
            // 주문 상태가 WAIT일 때만 상태를 변경합니다.
            setOrderStatus('DELIVERY');
        }
    };

    const buttonText = orderStatus === 'WAIT' ? '전송' : '전송 완료';
    const isDisabled = orderStatus === 'DELIVERY';
    function sendOrder(){
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

    return (
        <div>
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
                {orders.map(order => (
                    <tr id="order-row">
                        <td>{order.orderNumberId}</td>
                        <td id="order-number">{order.orderNumber}</td>
                        <td><button id="order-button" onClick={sendOrder} disabled={order.orderStatus === 'DELIVERY'}>{order.orderStatus}</button ></td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default OrderList;
