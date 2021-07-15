import { NbMenuItem } from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'News',
    icon: 'cast-outline',
    link: '/pages/news',
    home: true,
    data: {
      permission: 'view_page',
      resource: 'news',
    },
  },
  {
    title: 'Requests',
    icon: 'edit-2-outline',
    expanded: true,
    children: [
      {
        title: 'GarbageRemoval',
        link: '/pages/management/requests/garbageRemoval',
        icon: 'trash-2-outline',
      },
    ],
    data: {
      permission: 'view_page',
      resource: 'requests',
    },
  },
  {
    title: 'Management',
    group: true,
    data: {
      permission: 'view_page',
      resource: 'management',
    },
  },
  {
    title: 'Users',
    icon: 'people-outline',
    link: '/pages/management/users',
    data: {
      permission: 'view_page',
      resource: 'management/users',
    },
  },
  {
    title: 'Addresses',
    icon: 'home-outline',
    link: '/pages/management/addresses',
    data: {
      permission: 'view_page',
      resource: 'management/addresses',
    },
  },

];
