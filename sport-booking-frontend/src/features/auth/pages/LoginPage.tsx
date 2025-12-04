import { Link } from 'react-router-dom';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { LoginForm } from '../components/LoginForm';
import { Button } from '@/components/ui/button';

export const LoginPage = () => {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center gap-8 pt-12 px-4">
      <Card className="w-full max-w-lg">
        <CardHeader>
          <CardTitle className="text-2xl text-center">Đăng nhập</CardTitle>
        </CardHeader>
        <CardContent>
          <LoginForm />
          <div className='flex items-center justify-center mt-4'>
            <Link 
              tabIndex={4} 
              to={'/forgot-password'} 
              className='w-fit text-sm hover:underline font-medium text-center'>
                Quên mật khẩu?
            </Link>
          </div>
        </CardContent>
      </Card>
      <p className="text-center text-sm font-light text-white/70 flex items-center justify-center gap-1">
        Chưa có tài khoản?{' '}
        <Link to="/register" className="hover:underline text-sm font-medium text-white">
          Đăng ký ngay
        </Link>
      </p>
      <div className='flex items-center justify-center gap-4'>
        <hr className='border-gray-300 w-20' />
        <p className='text-center text-sm font-light text-white/70'>Hoặc</p>
        <hr className='border-gray-300 w-20' />
      </div>
      <Button className="w-full max-w-lg bg-white/10 border border-white/20 hover:bg-white/20">
        Đăng nhập với Google
      </Button>
    </div>
  );
};