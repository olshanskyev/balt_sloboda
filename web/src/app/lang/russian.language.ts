
export class RussianLanguage {

    public static getTranslations(): Object {
        return {
            login: {
                email: 'Электронная почта',
                emailAdress: 'Адрес электронной почты',
                password: 'Пароль',
                forgotPassword: 'Забыли пароль?',
                rememberMe: 'Запомнить',
                helloLogin: 'Привет! Укажите E-mail для входа',
                login: 'Вход',
                emailRequired: 'Введите адрес электронной почты',
                realEmail: 'Необходимо ввести настоящий адрес',
                passwordRequired: 'Необходимо ввести пароль',
                passwordShouldContain: 'Пароль должен содержать от {{from}} до {{to}} символов',
                Log_In: 'Войти',
                dontHaveAccount: 'Ещё нет аккаунта?',
                register: 'Зарегистрироваться',
                hooray: 'Ура!',
                snap: 'Ошибочка вышла',
            },
            register: {
                register: 'Зарегистрироваться',
                successMessage: 'Письмо с запросом на регистрацию отправлено. Ожидайте подтверждения на email',
                firstName: 'Имя',
                lastName: 'Фамилия',
                firstNameRequired: 'Введите имя',
                lastNameRequired: 'Введите фамилию',
                firstNameShouldContain: 'Поле Имя должно содержать от {{from}} до {{to}} символов',
                lastNameShouldContain: 'Поле Фамилия должно содержать от {{from}} до {{to}} символов',
                alreadyHaveAccount: 'Уже зарегистрированы',
                agreeTo: 'Согласен с',
                termsAndConditions: ' Условиями пользования',
                selectStreet: 'Выберите улицу',
                street: 'Улица',
                home: 'Дом',
                plot: 'участок',
                selectHome: 'Выберите дом',

            },
            menu: {
                News: 'Новости',
                Requests: 'Заявки',
                GarbageRemoval: 'Вывоз мусора',
                Management: 'Управление',
                Users: 'Жители',
                Addresses: 'Адреса',
                LogOut: 'Выйти',
                Profile: 'Профиль',
            },
            errors: {
                errorCode: 'Код ошибки',
                cannotGetUsers: 'Не удается получить список жителей',
                cannotGetStreet: 'Не удается загрузить список улиц',
                cannotGetAddresses: 'Не удается загрузить список адресов',
            },
            usersPage: {
                users: 'Жители',
                user: 'Имя пользователя',
                newRequests: 'Новые заявки',
                count: 'Всего',
                firstName: 'Имя',
                lastName: 'Фамилия',

            },
        };
    }
}
