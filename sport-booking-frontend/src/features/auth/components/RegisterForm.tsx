import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useRegister } from '../hooks/useRegister';
import { registerSchema, type RegisterFormData } from '../schemas/register.schema';

export const RegisterForm = () => {
  const { mutate: register, isPending, error } = useRegister();

  const {
    register: field,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  });

  const onSubmit = (data: RegisterFormData) => {
    // Loại bỏ confirmPassword trước khi gửi BE
    const { confirmPassword, ...payload } = data;
    register(payload);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <Label htmlFor="username">Username</Label>
        <Input id="username" {...field('username')} />
        {errors.username && (
          <p className="text-sm text-red-600 mt-1">{errors.username.message}</p>
        )}
      </div>

      <div>
        <Label htmlFor="email">Email</Label>
        <Input id="email" type="email" {...field('email')} />
        {errors.email && (
          <p className="text-sm text-red-600 mt-1">{errors.email.message}</p>
        )}
      </div>

      <div>
        <Label htmlFor="fullName">Họ và tên</Label>
        <Input id="fullName" {...field('fullName')} />
        {errors.fullName && (
          <p className="text-sm text-red-600 mt-1">{errors.fullName.message}</p>
        )}
      </div>

      <div>
        <Label htmlFor="phoneNumber">Số điện thoại</Label>
        <Input id="phoneNumber" {...field('phoneNumber')} />
        {errors.phoneNumber && (
          <p className="text-sm text-red-600 mt-1">{errors.phoneNumber.message}</p>
        )}
      </div>

      <div>
        <Label htmlFor="password">Mật khẩu</Label>
        <Input id="password" type="password" {...field('password')} />
        {errors.password && (
          <p className="text-sm text-red-600 mt-1">{errors.password.message}</p>
        )}
      </div>

      <div>
        <Label htmlFor="confirmPassword">Xác nhận mật khẩu</Label>
        <Input id="confirmPassword" type="password" {...field('confirmPassword')} />
        {errors.confirmPassword && (
          <p className="text-sm text-red-600 mt-1">{errors.confirmPassword.message}</p>
        )}
      </div>

      {error && <p className="text-sm text-red-600">{error.message}</p>}

      <Button type="submit" disabled={isPending} className="w-full">
        {isPending ? 'Đang đăng ký...' : 'Đăng ký'}
      </Button>
    </form>
  );
};