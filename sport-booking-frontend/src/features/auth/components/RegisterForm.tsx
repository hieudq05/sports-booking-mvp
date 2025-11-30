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
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      <div className='flex flex-col gap-2'>
        <Label htmlFor="username" className='ml-2 text-muted-foreground'>Username</Label>
        <Input id="username" {...field('username')} placeholder="Nhập tên người dùng"/>
        {errors.username && (
          <p className="text-sm text-red-600">{errors.username.message}</p>
        )}
      </div>

      <div className='flex flex-col gap-2'>
        <Label htmlFor="email" className='ml-2 text-muted-foreground'>Email</Label>
        <Input id="email" type="email" {...field('email')} placeholder="Nhập email" />
        {errors.email && (
          <p className="text-sm text-red-600 mt-1">{errors.email.message}</p>
        )}
      </div>

      <div className='flex flex-col gap-2'>
        <Label htmlFor="fullName" className='ml-2 text-muted-foreground'>Họ và tên</Label>
        <Input id="fullName" {...field('fullName')} placeholder="Nhập họ và tên" />
        {errors.fullName && (
          <p className="text-sm text-red-600 mt-1">{errors.fullName.message}</p>
        )}
      </div>

      <div className='flex flex-col gap-2'>
        <Label htmlFor="phoneNumber" className='ml-2 text-muted-foreground'>Số điện thoại</Label>
        <Input id="phoneNumber" {...field('phoneNumber')} placeholder="Nhập số điện thoại" />
        {errors.phoneNumber && (
          <p className="text-sm text-red-600 mt-1">{errors.phoneNumber.message}</p>
        )}
      </div>

      <div className='flex gap-4'>
        <div className='flex flex-col gap-2 w-full'>
          <Label htmlFor="password" className='ml-2 text-muted-foreground'>Mật khẩu</Label>
          <Input id="password" type="password" {...field('password')} placeholder="Nhập mật khẩu"/>
          {errors.password && (
            <p className="text-sm text-red-600 mt-1">{errors.password.message}</p>
          )}
        </div>

        <div className='flex flex-col gap-2 w-full'>
          <Label htmlFor="confirmPassword" className='ml-2 text-muted-foreground'>Xác nhận mật khẩu</Label>
          <Input id="confirmPassword" type="password" {...field('confirmPassword')} placeholder="Nhập lại mật khẩu"/>
          {errors.confirmPassword && (
            <p className="text-sm text-red-600 mt-1">{errors.confirmPassword.message}</p>
          )}
        </div>
      </div>

      {error && <p className="text-sm text-red-600">{error.message}</p>}

      <Button type="submit" disabled={isPending} className="w-full">
        {isPending ? 'Đang đăng ký...' : 'Đăng ký'}
      </Button>
    </form>
  );
};