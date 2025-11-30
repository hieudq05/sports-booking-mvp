import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useLogin } from '../hooks/useLogin';
import { loginSchema, type LoginFormData } from '../schemas/login.schema';

export const LoginForm = () => {
  const { mutate: login, isPending, error } = useLogin();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = (data: LoginFormData) => {
    login(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <Label htmlFor="username">Username</Label>
        <Input
          id="username"
          {...register('username')}
          placeholder="Nhập username"
        />
        {errors.username && (
          <p className="text-sm text-red-600 mt-1">
            {errors.username.message}
          </p>
        )}
      </div>

      <div>
        <Label htmlFor="password">Mật khẩu</Label>
        <Input
          id="password"
          type="password"
          {...register('password')}
          placeholder="Nhập mật khẩu"
        />
        {errors.password && (
          <p className="text-sm text-red-600 mt-1">
            {errors.password.message}
          </p>
        )}
      </div>

      {error && (
        <p className="text-sm text-red-600">
          {error.message}
        </p>
      )}

      <Button type="submit" disabled={isPending} className="w-full">
        {isPending ? 'Đang đăng nhập...' : 'Đăng nhập'}
      </Button>
    </form>
  );
};