
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
                tryAgain: 'Попробуйте еще раз',
                successMessage: 'Вход выполнен',
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
            resetPass: {
                newPassword: 'Новый пароль',
                setNewPassword: 'Пожалуйста, задайте новый пароль',
                successMessage: 'Пароль успешно изменен',
                passwordConfirmRequired: 'Необоходимо подтвердить пароль',
                passwordsNotMatch: 'Пароли не совпадают',
                confirmPassword: 'Подтвердить пароль',
                somethingWentWrong: 'Что-то пошло не так, невозможно задать новый пароль. Для восстановления пароля перейдите по ссылке из письма.',
                changePassword: 'Изменить пароль',
                backToLogin: 'Вернуться на страницу входу',
            },
            requestPass: {
                successMessage: 'Запрос на восстановление пароля выслан на почту',
                forgotPassword: 'Забыли пароль?',
                somethingWentWrong: 'Что-то пошло не так, обратитесь к администратору.',
                enterEmail: 'Введите ваш email',
                requestPassword: 'Запросить пароль',
                enterEmailWillSendPass: 'Введите ваш адресс электронной почты и мы вышлем ссылку для задания нового пароля',
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
                RequestManager: 'Менеджер заявок',
            },
            errors: {
                error: 'Ошибка',
                errorCode: 'Код ошибки',
                cannotGetUsers: 'Не удается получить список жителей',
                cannotGetRequests: 'Не удается получить лист заявок',
                cannotGetStreet: 'Не удается загрузить список улиц',
                cannotGetAddresses: 'Не удается загрузить список адресов',
                userAlreadyExists: 'Пользователь {{user}} уже зарегистрирован',
                serverCommunicationError: 'Ошибка связи с сервером',
                Unauthorized: 'Пользователь не авторизован',
                Forbidden: 'Доступ запрещен',
                badCredentials: 'Неверные логин/пароль',
                addressAlreadyUsed: 'Адрес уже зарегистрирован за другим пользователем',
                notExistingAddress: 'Адрес не существует',
                newUserRequestAlreadyExists: 'Запрос на регистрацию для пользователя {{user}} уже отправлен',
                newUserRequestNotFound: 'Запрос на регистрацию пользователя не найден',
                userInfoNotFound: 'Информация о пользователе {{user}} не найдена',
                resetPasswordRequestNotFound: 'Запрос на восстановление пароля не найден',
                passwordsNotMatch: 'Введенные пароли не совпадают',
                userNotFound: 'Пользователь {{user}} не найден',
                tokenNotValidOrExpired: 'Запрос на восстановление пароля истек',
                emptyPasswordResetToken: 'Неверный запрос на восстановление пароля',
            },
            residentsPage: {
                residents: 'Жители',
                user: 'Имя пользователя',
                newRequests: 'Новые заявки',
                count: 'Всего',
                firstName: 'Имя',
                lastName: 'Фамилия',
                address: 'Адрес',
                st: 'ул.',
                h: 'д.',
                pl: 'уч.',
                status: 'Состояние заявки',
                accept: 'Принять зарос',
                decline: 'Отклонить запрос',
                shureAccept: 'Принять запрос от пользователя {{user}}?'
            },
            addressesPage: {
                addresses: 'Адреса',
                street: 'Улица',
                houseNumber: 'Номер дома',
                plotNumber: 'Номер участка',
            },
            requestManagerPage: {
                allRequestTypes: 'Все заявки',
                createNewRequestType: 'Создать новую заявку',
                parameterInput: 'Ввод параметров',
                mainInformation: 'Общая информация',
                title: 'Заголовок',
                titleRequired: 'Необходимо ввести заголовок',
                description: 'Описание',
                enterDescription: 'Введите описание',
                perform: 'Выполняется',
                once: 'Однократно',
                multipleTimes: 'Многократно',
                view: 'Отображение',
                Weekly: 'Еженедельно',
                Monthly: 'Ежемесячно',
                selectionMode: 'Периодичность',
                Manually: 'Задать вручную',
                daysOfWeek: 'Дни недели',
                every: 'Каждый',
                first: 'Первый',
                second: 'Второй',
                third: 'Третий',
                fourth: 'Четвертый',
                last: 'Последний',
                add: 'Добавить',
                periodicity: 'Периодичность',
                parameterName: 'Имя параметра',
                paramNameRequired: 'Необходимо ввести имя параметра',
                comment: 'Комментарий',
                enterParamComment: 'Введите комментарий к параметру'


            },
            common: {
                next: 'Далее',
                prev: 'Назад',
                create: 'Создать',
                Monday: 'Понедельник',
                Tuesday: 'Вторник',
                Wednesday: 'Среда',
                Thursday: 'Четверг',
                Friday: 'Пятница',
                Saturday: 'Суббота',
                Sunday: 'Воскресенье',
                day: 'День',
            }
        };
    }
}
