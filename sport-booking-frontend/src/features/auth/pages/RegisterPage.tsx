import { Link } from 'react-router-dom';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { RegisterForm } from '../components/RegisterForm';

export const RegisterPage = () => {
  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-8 bg-[url('/src/assets/image/login-bg.jpg')] bg-cover bg-center bg-no-repeat">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle className="text-2xl text-center">Đăng ký</CardTitle>
        </CardHeader>
        <CardContent>
          <RegisterForm />
          <p className="text-center text-sm text-gray-600 mt-4">
            Đã có tài khoản?{' '}
            <Link to="/login" className="hover:underline text-sm font-medium text-foreground">
              Đăng nhập
            </Link>
          </p>
        </CardContent>
      </Card>
    </div>
  );
};