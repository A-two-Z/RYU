import React, { useState, useEffect } from 'react';
import '../../assets/ProductList.scss';

function ProductList() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [orderQuantities, setOrderQuantities] = useState({});

    useEffect(() => {
        fetch(process.env.REACT_APP_AWS_URI+'/product')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setProducts(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error);
                setLoading(false);
            });
    }, []);

    function generateOrderNumber() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');
        const milliseconds = String(now.getMilliseconds()).padStart(3, '0'); // 3자리로 패딩
        return `${year}${month}${day}${hours}${minutes}${seconds}${milliseconds}`;
    }


    function submitOrder() {
        const orders = [];
        const checkboxes = document.querySelectorAll('input[type="checkbox"]:checked');
        const orderNumber = generateOrderNumber();

        checkboxes.forEach((checkbox) => {
            const productId = checkbox.value;
            const quantityInput = document.querySelector(`input[name="quantity_${productId}"]`);
            const quantity = quantityInput.value;

            if (quantity != 0) {
                orders.push({productId: productId, quantity: quantity});
            }
        });

        if(orders.length == 0){
            alert("주문하신 물품이 없습니다.");
            return;
        }

        const orderData = {
            orders: orders,
            orderNum: orderNumber
        };

        const orderDataString = JSON.stringify(orderData, null, 2);

        console.log(orderDataString);

        // 데이터 전송
        fetch(process.env.REACT_APP_AWS_URI+'/order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: orderDataString
        })
            .then(response => {

                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }else if(response.status == 202){
                    alert("주문 전송에 성공했습니다.")
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('주문 전송에 실패했습니다.');
            });
    }
    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;
    return (
        <div className='productlist'>
            <h1>Product List <sub>(administrator)</sub></h1>
            <table>
                <thead>
                <tr>
                    <td colSpan="4">주문하기</td>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>체크박스</td>
                    <td>이름</td>
                    <td>수량</td>
                </tr>
                {products.map(product => (
                    <tr key={product.productId}>
                        <td><input type="checkbox" name="order" value={product.productId} /></td>
                        <td>{product.productName}</td>
                        <td><input type="text" name={`quantity_${product.productId}`} /></td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={submitOrder}>주문 제출</button>

        </div>
    );
}

export default ProductList;
