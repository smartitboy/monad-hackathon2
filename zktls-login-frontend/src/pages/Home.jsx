import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { API_CONFIG } from '../config/config';
import { message } from 'antd';
import { UserOutlined } from '@ant-design/icons';

export default function Home() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();
    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
        message.success('注销成功');
        navigate('/login');
    };
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            message.warning('请先登录');
            navigate('/login');
            return;
        }
        fetch(`${API_CONFIG.BASE_URL}/api/login/check`, {
            method: 'GET',
            headers: {
                'token': `${token}`,
            },
        })
            .then(async res => {
                if (res.ok) {
                    const data = await res.json();
                    if (data.rc === "-1" && data.mc === "NOT_LOGIN") {
                        message.error(data.msg);
                        navigate('/login');
                    } else {
                        setUser(data.result);
                    }
                } else {
                    message.error('登录已失效，请重新登录');
                    navigate('/login');
                }
            })
            .catch(() => {
                message.error('网络错误，请重新登录');
                navigate('/login');
            });
    }, [navigate]);

    if (!user) {
        return <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-indigo-100 via-blue-100 to-purple-100">加载中...</div>;
    }

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-indigo-100 via-blue-100 to-purple-100">
            <div className="bg-white/90 backdrop-blur-md p-10 rounded-3xl shadow-2xl border border-indigo-100 flex flex-col items-center mt-20 animate-fade-in">
                <div className="bg-gradient-to-tr from-indigo-500 to-blue-400 rounded-full p-4 shadow-lg mb-4">
                    <UserOutlined style={{ fontSize: 48, color: '#fff' }} />
                </div>
                <h1 className="text-3xl font-extrabold text-gray-800 mb-2 animate-slide-in">欢迎，<span className="bg-gradient-to-r from-indigo-500 to-blue-400 bg-clip-text text-transparent">{user}</span></h1>
                <div className="text-gray-500 text-lg mb-4 animate-fade-in">恭喜您通过 <span className="font-bold text-indigo-500">ZKTLS</span> 验证，这里是您的首页。</div>
                <div className="mt-6 flex gap-4">
                    <a href="#" onClick={logout} className="px-6 py-2 rounded-full bg-white border border-indigo-300 text-indigo-600 font-semibold shadow hover:bg-indigo-50 transition">退出登录</a>
                </div>
            </div>
            <style>{`
                .animate-fade-in { animation: fadeIn 1s; }
                .animate-slide-in { animation: slideIn 0.8s; }
                @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
                @keyframes slideIn { from { opacity: 0; transform: translateY(30px);} to { opacity: 1; transform: none; } }
            `}</style>
        </div>
    );
} 