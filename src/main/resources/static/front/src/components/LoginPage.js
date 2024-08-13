// LoginPage.js
import React, { useState } from 'react';
import axios from 'axios';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/login', { username, password });
            const { token } = response.data;
            localStorage.setItem('jwtToken', token); // JWT 토큰을 로컬 스토리지에 저장
            window.location.href = '/orders'; // 주문 페이지로 리디렉션
        } catch (error) {
            console.error('Login failed:', error);
            alert('로그인 실패');
        }
    };

    return (
        <div>
            <h1>로그인</h1>
            <form onSubmit={handleLogin}>
                <input
                    type="text"
                    placeholder="사용자명"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit">로그인</button>
            </form>
        </div>
    );
}

export default LoginPage;
