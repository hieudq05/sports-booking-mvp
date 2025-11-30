import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
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
      <div className='flex flex-col gap-2'>
        <Input
          id="username"
          disabled={isPending}
          {...register('username')}
          placeholder="Tên người dùng"
          tabIndex={1}
        />
        {errors.username && (
          <p className="text-sm text-red-600 mt-1 w-full flex items-center gap-2">
            <div className='size-3.5 rounded-full bg-red-500/30 flex justify-center items-center'>
              <div className='size-1.5 rounded-full bg-red-500'></div>
            </div>
            {errors.username.message}
          </p>
        )}
      </div>

      <div className='flex flex-col gap-2 items-end'>
        <Input
          id="password"
          type="password"
          disabled={isPending}
          {...register('password')}
          placeholder="Mật khẩu"
          tabIndex={2}
        />
        {errors.password && (
          <p className="text-sm text-red-600 mt-1 w-full flex items-center gap-2">
            <div className='size-3.5 rounded-full bg-red-500/30 flex justify-center items-center'>
              <div className='size-1.5 rounded-full bg-red-500'></div>
            </div>
            {errors.password.message}
          </p>
        )}
      </div>

      {error && (
        <p className="text-sm text-red-600 mt-1 w-full flex items-center gap-2">
            <div className='size-3.5 rounded-full bg-red-500/30 flex justify-center items-center'>
              <div className='size-1.5 rounded-full bg-red-500'></div>
            </div>
            {error.message}
          </p>
      )}

      <Button type="submit" disabled={isPending} className="w-full" tabIndex={3}>
        {isPending ? 'Đang đăng nhập...' : 'Đăng nhập'}
      </Button>
    </form>
  );
};