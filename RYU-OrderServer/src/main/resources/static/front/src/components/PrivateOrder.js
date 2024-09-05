// PrivateRoute.js
import React from 'react';
import { Route, Redirect } from 'react-router-dom';

const PrivateOrder = ({ component: Component, ...rest }) => {
    const token = localStorage.getItem('jwtToken'); // JWT 토큰 확인

    return (
        <Route
            {...rest}
            render={props =>
                token ? (
                    <Component {...props} />
                ) : (
                    <Redirect to="/login" /> // 로그인 페이지로 리디렉션
                )
            }
        />
    );
};

export default PrivateRoute;
