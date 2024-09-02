import React, { useState, useEffect } from 'react';
import '../../assets/ProductList.scss';

function ProductList() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [orderQuantities, setOrderQuantities] = useState({});

    useEffect(() => {
        fetch(process.env.REACT_APP_AWS_URI+':8080/productSector')
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

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;
    return (
        <div>
            <h1>Product List</h1>
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
    )
}

export default ProductList;
