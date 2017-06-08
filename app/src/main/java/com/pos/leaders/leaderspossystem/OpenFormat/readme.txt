
                try {
                    OpenFrmt openFrmt = new OpenFrmt();
                    BKMVDATA bkmvdata = new BKMVDATA(openFrmt.getBKMVDATA(), SplashScreenActivity.this);
                    int row=bkmvdata.make();

                    INI ini = new INI(openFrmt.getINI(), SplashScreenActivity.this, row, bkmvdata.getFirstDate(), new Date().getTime());
                    ini.make();

                    openFrmt.Compress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
