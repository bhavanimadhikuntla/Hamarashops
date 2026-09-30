import React, { useState } from 'react';
import { MessageCircle, X, Send, Bot, User } from 'lucide-react';
import { apiClient } from '../../services/apiClient';

const Chatbot = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [message, setMessage] = useState('');
    const [messages, setMessages] = useState([
        {
            sender: 'bot',
            text: 'Hello! I am the HamaraShops.ai AI Assistant. How can I help you today?',
            agent: 'Company Agent',
        },
    ]);
    const [isLoading, setIsLoading] = useState(false);

    const sendMessage = async () => {
        const trimmedMessage = message.trim();

        if (!trimmedMessage || isLoading) {
            return;
        }

        setMessages((previousMessages) => [
            ...previousMessages,
            {
                sender: 'user',
                text: trimmedMessage,
            },
        ]);

        setMessage('');
        setIsLoading(true);

        try {
            const response = await apiClient.post('/chat', {
                message: trimmedMessage,
            });

            setMessages((previousMessages) => [
                ...previousMessages,
                {
                    sender: 'bot',
                    text: response.message,
                    agent: response.agent,
                },
            ]);
        } catch (error) {
            setMessages((previousMessages) => [
                ...previousMessages,
                {
                    sender: 'bot',
                    text:
                        error?.message ||
                        'Sorry, I could not connect to the AI assistant.',
                    agent: 'System',
                },
            ]);
        } finally {
            setIsLoading(false);
        }
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }
    };

    return (
        <>
            {!isOpen && (
                <button
                    type="button"
                    onClick={() => setIsOpen(true)}
                    aria-label="Open AI Chatbot"
                    className="fixed bottom-6 right-6 z-50 flex h-14 w-14 items-center justify-center rounded-full bg-black text-white shadow-xl transition-transform duration-200 hover:scale-110"
                >
                    <MessageCircle size={26} />
                </button>
            )}

            {isOpen && (
                <div className="fixed bottom-6 right-6 z-50 flex h-[600px] w-[380px] max-w-[calc(100vw-2rem)] flex-col overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-2xl">

                    {/* Header */}
                    <div className="flex items-center justify-between bg-black px-5 py-4 text-white">
                        <div className="flex items-center gap-3">
                            <div className="flex h-10 w-10 items-center justify-center rounded-full bg-white text-black">
                                <Bot size={22} />
                            </div>

                            <div>
                                <h3 className="text-sm font-semibold">
                                    HamaraShops.ai
                                </h3>

                                <p className="text-xs text-gray-300">
                                    AI Assistant
                                </p>
                            </div>
                        </div>

                        <button
                            type="button"
                            onClick={() => setIsOpen(false)}
                            aria-label="Close AI Chatbot"
                            className="rounded-full p-2 transition hover:bg-white/10"
                        >
                            <X size={20} />
                        </button>
                    </div>

                    {/* Messages */}
                    <div className="flex-1 space-y-4 overflow-y-auto bg-gray-50 p-4">
                        {messages.map((item, index) => (
                            <div
                                key={index}
                                className={`flex ${item.sender === 'user'
                                        ? 'justify-end'
                                        : 'justify-start'
                                    }`}
                            >
                                <div
                                    className={`max-w-[82%] rounded-2xl px-4 py-3 text-sm ${item.sender === 'user'
                                            ? 'rounded-br-sm bg-black text-white'
                                            : 'rounded-bl-sm border border-gray-200 bg-white text-gray-800'
                                        }`}
                                >
                                    <div className="mb-1 flex items-center gap-2 text-xs opacity-70">
                                        {item.sender === 'user' ? (
                                            <>
                                                <User size={13} />
                                                You
                                            </>
                                        ) : (
                                            <>
                                                <Bot size={13} />
                                                {item.agent || 'AI Assistant'}
                                            </>
                                        )}
                                    </div>

                                    <p className="whitespace-pre-wrap leading-relaxed">
                                        {item.text}
                                    </p>
                                </div>
                            </div>
                        ))}

                        {isLoading && (
                            <div className="flex justify-start">
                                <div className="rounded-2xl rounded-bl-sm border border-gray-200 bg-white px-4 py-3 text-sm text-gray-500">
                                    AI Assistant is thinking...
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Input */}
                    <div className="border-t border-gray-200 bg-white p-3">
                        <div className="flex items-end gap-2 rounded-xl border border-gray-300 bg-gray-50 p-2">
                            <textarea
                                value={message}
                                onChange={(event) => setMessage(event.target.value)}
                                onKeyDown={handleKeyDown}
                                placeholder="Ask me anything..."
                                rows={1}
                                className="max-h-24 flex-1 resize-none bg-transparent px-2 py-2 text-sm outline-none"
                            />

                            <button
                                type="button"
                                onClick={sendMessage}
                                disabled={!message.trim() || isLoading}
                                aria-label="Send message"
                                className="flex h-10 w-10 items-center justify-center rounded-lg bg-black text-white transition hover:bg-gray-800 disabled:cursor-not-allowed disabled:opacity-40"
                            >
                                <Send size={18} />
                            </button>
                        </div>

                        <p className="mt-2 text-center text-[10px] text-gray-400">
                            Powered by HamaraShops.ai AI
                        </p>
                    </div>
                </div>
            )}
        </>
    );
};

export default Chatbot;
