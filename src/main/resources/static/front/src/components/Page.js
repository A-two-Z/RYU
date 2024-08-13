import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Nav from './Nav';
import ProductList from './productList/ProductList';
import OrderList from './orderList/OrderList';
import LoginPage from "./LoginPage";

const Page = () => {
    return (
        <div>
            <Nav />
            <Routes>
                <Route path="/product" element={<ProductList />} />
                <Route path="/order" element={<OrderList />} />
                <Route path="/login" element={<LoginPage />} />
            </Routes>
        </div>
    );
};

export default Page;
