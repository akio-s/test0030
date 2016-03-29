package jp.co.token.optout;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.naming.*;
import javax.sql.DataSource;

import com.ibm.websphere.ce.cm.StaleConnectionException;

import jp.co.token.optout.util.UTBLogManager;
import jp.co.token.optout.util.UTBMessageLoder;

/**
 * データソースビーン.
 * <pre>
 * コネクションプーリングを行う為に、データソースを取得しキャッシュする。
 * キャッシュしたデータソースより、DB接続の取得を行う。
 * DB接続、ステートメント、結果セットを解放するメソッドを実装する。
 * </pre>
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WIBDataSourceLookup {
    /** コネクションプーリング使用フラグ true:使用 false:未使用*/
    private static boolean pooling = false;
    /** JDBCドライバークラス名 */
    private static String jdbcClassName;
    /** データベースアクセスURL */
    private static String jdbcUrl;
    /** データベースアクセスシステム名 */
    private static String dbSystem;
    /** プログラムオブジェクライブラリー名 */
    private static String pgmObject;
    /** データソース */
    private static DataSource poolDataSource;
    /** データソース名 */
    private static String poolDataSourceName;
    /** データソースに対するDBのユーザーID */
    private static String poolDataSourceUser;
    /** データソースに対するDBのパスワード */
    private static String poolDataSourcePass;
    /** データライブラリー名１ */
    private static String poolDataLibrary1;
    /** データライブラリー名２ */
    private static String poolDataLibrary2;
    /** データライブラリー名３ */
    private static String poolDataLibrary3;

    /**
     * コンストラクタ.
     *
     */
    public WIBDataSourceLookup(){
    }

    /**
     * コネクションプーリング使用フラグのgetter.
     * 
     * @return コネクションプーリング使用フラグ
     */
    public boolean getPooling() {
        return pooling;
    }

    /**
     * JDBCドライバークラス名のgetter.
     *
     * @return JDBCドライバークラス名
     */
    public String getJdbcClassName() {
        return jdbcClassName;
    }

    /**
     * データベースアクセスURLのgetter.
     *
     * @return データベースアクセスURL 
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    /**
     * データベースアクセスシステム名のgetter.
     *
     * @return データベースアクセスシステム名
     */
    public String getDbSystem() {
        return dbSystem;
    }

    /**
     * プログラムオブジェクライブラリー名のgetter.
     *
     * @return プログラムオブジェクライブラリー名
     */
    public String getPgmObject() {
        return pgmObject;
    }

    /**
     * データソースのgetter.
     *
     * @return データソース
     */
    public DataSource getPoolDataSource() {
        return poolDataSource;
    }

    /**
     * データソース名のgetter.
     *
     * @return データソース名
     */
    public String getPoolDataSourceName() {
        return poolDataSourceName;
    }

    /**
     * データソースに対するDBのユーザーIDのgetter.
     *
     * @return データソースに対するDBのユーザーID
     */
    public String getPoolDataSourceUser() {
        return poolDataSourceUser;
    }

    /**
     * データソースに対するDBのパスワードのgetter.
     *
     * @return データソースに対するDBのパスワード
     */
    public String getPoolDataSourcePass() {
        return poolDataSourcePass;
    }

    /**
     * データライブラリー名１のgetter.
     *
     * @return データライブラリー名１
     */
    public String getPoolDataLibrary1() {
        return poolDataLibrary1;
    }

    /**
     * データライブラリー名２のgetter.
     *
     * @return データライブラリー名２
     */
    public String getPoolDataLibrary2() {
        return poolDataLibrary2;
    }

    /**
     * データライブラリー名３のgetter.
     *
     * @return データライブラリー名３
     */
    public String getPoolDataLibrary3() {
        return poolDataLibrary3;
    }

    /**
     * 初期処理.
     * <pre>
     * システムプロパティファイルより各種接続情報を取得する
     * wss.db.pooling      :コネンクションプーリング使用フラグ
     * wss.db.jdbc.class   :JDBCドライバークラス名(AS/400)
     * wss.db.jdbc.url     :データベースアクセスURL(AS/400)
     * wss.db.datasource   :データソースに対するDBのユーザーID(AS/400)
     * wss.db.user         :データソースに対するDBのユーザーID(AS/400)
     * wss.db.pass         :データソースに対するDBのパスワード(AS/400)
     * wss.db.system       :データベースアクセスシステム名(AS/400)
     * wss.db.pgmobject    :プログラムオブジェクライブラリー名(AS/400)
     * wss.db.datalibrary  :データライブラリー名(AS/400)
     * wss.oradb.jdbc.class:JDBCドライバークラス名(Oracle)
     * wss.oradb.jdbc.url  :データベースアクセスURL(Oracle)
     * wss.oradb.datasource:データソース名(Oracle)
     * wss.oradb.user      :データソースに対するDBのユーザーID(Oracle)
     * wss.oradb.pass      :データソースに対するDBのパスワード(Oracle)
     * </pre>
     *
     * @return true:正常終了 false:異常終了
     */
    public boolean initializeDataSource() {
        //コネクションプーリングの有効・無効フラグを取得する。（"true"：使用 "false"：未使用 ）
        String pflg         = UTBMessageLoder.getSystemProperty("wss.db.pooling");
        jdbcClassName       = UTBMessageLoder.getSystemProperty("wss.db.jdbc.class");
        jdbcUrl             = UTBMessageLoder.getSystemProperty("wss.db.jdbc.url");
        poolDataSourceName  = UTBMessageLoder.getSystemProperty("wss.db.datasource");
        poolDataSourceUser  = UTBMessageLoder.getSystemProperty("wss.db.user");
        poolDataSourcePass  = UTBMessageLoder.getSystemProperty("wss.db.pass");
        dbSystem            = UTBMessageLoder.getSystemProperty("wss.db.system");
        pgmObject           = UTBMessageLoder.getSystemProperty("wss.db.pgmobject");
        poolDataLibrary1    = UTBMessageLoder.getSystemProperty("wss.db.datalibrary1");
        poolDataLibrary2    = UTBMessageLoder.getSystemProperty("wss.db.datalibrary2");
        poolDataLibrary3    = UTBMessageLoder.getSystemProperty("wss.db.datalibrary3");

        // 何れかの値がnullであれば以上終了
        if (pflg == null || jdbcClassName == null || jdbcUrl == null ||
            poolDataSourceName == null || poolDataSourceUser == null ||
            poolDataSourcePass == null || dbSystem == null || 
            pgmObject == null || poolDataLibrary1 == null || 
            poolDataLibrary2 == null || poolDataLibrary3 == null) {
                return false;
        }

        // コネクションプーリング
        if (pflg.equals("true")) {
            pooling = true;
        } else if (pflg.equals("false")) {
            pooling = false;
        } else {
            return false;
        }
        //テストモードの場合
        if (!pooling) {
            UTBLogManager.writeLog( "コネクションプーリングを使用せずにＤＢ接続を行います。" );
            return true;
        }

        //キャッシュがnullならばデータソースの新規ルックアップを行う。
        if (poolDataSource == null) {
            poolDataSource = lookupDataSource(poolDataSourceName);
            //取得できなかった場合はエラー
            if (poolDataSource == null) {
                UTBLogManager.writeLog("AS_DB_ERR");
                return false;
            }
        }

        return true;
    }

    /**
     * コネクションの取得(AS/400).
     * <pre>
     * 指定したデータソースより、コネクションを取得する
     * </pre>
     *
     * @return DBコネクション
     */
    public Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            //テストモードの場合は直接接続を行う。
            if (!pooling) {
                Class.forName(jdbcClassName);
                con = DriverManager.getConnection(jdbcUrl, 
                                      poolDataSourceUser, poolDataSourcePass);
            //本番モードの場合はコネクションをキャッシュより取得する。
            } else {
                con = poolDataSource.getConnection(poolDataSourceUser, 
                                                   poolDataSourcePass);
            }
        } catch (Exception e) {
            UTBLogManager.writeLog(this.getClass(), e.toString());
            throw new SQLException(this.getClass().getName() + 
                                    "コネクションの取得に失敗しました。");
        }
        return con;
    }

    /**
     * 結果セットの開放.
     *
     * @param aRs 結果セット
     * @exception SQLException SQL例外発生時
     */
    public void closeResultSet(ResultSet aRs) throws SQLException {
        try{
            if (aRs != null) {
                aRs.close();
            }
        } catch (StaleConnectionException se) {
            UTBLogManager.writeLog(this.getClass(), se.toString());
            se.printStackTrace();
            throw new SQLException(this.getClass().getName() + 
                                    "StaleConnectionException");
        } catch (SQLException e) {
            UTBLogManager.writeLog(this.getClass(), e.toString());
            e.printStackTrace();
        }
    }

    /**
     * ステートメントの開放.
     *
     * @param aStmt ステートメント
     * @exception SQLException SQL例外発生時
     */
    public void closeStatement(Statement aStmt) throws SQLException {
        try{
            if (aStmt != null) {
                aStmt.close();
            }
        } catch (StaleConnectionException se) {
            UTBLogManager.writeLog(this.getClass(), se.toString());
            se.printStackTrace();
            throw new SQLException(this.getClass().getName() + 
                                    "StaleConnectionException");
        } catch(SQLException e) {
            UTBLogManager.writeLog(this.getClass(), e.toString());
            e.printStackTrace();
        }
    }

    /**
     * コネクションの開放.
     *
     * @param aCon DBコネクション
     * @exception SQLException SQL例外発生時
     */
    public void closeConnection(Connection aCon) throws SQLException {
        try{
            if (aCon != null) {
                aCon.rollback();
                aCon.close();
            }
        } catch (StaleConnectionException se) {
            UTBLogManager.writeLog(this.getClass(), se.toString());
            se.printStackTrace();
            throw new SQLException(this.getClass().getName() + 
                                    "StaleConnectionException");
        } catch(SQLException e) {
            UTBLogManager.writeLog(this.getClass(), e.toString());
            e.printStackTrace();
        }
    }

    /**
     * データソースの取得.
     *
     * @param aDataSourceName 取得するデータソース名称
     * @return 取得したデータソース
     */
    private DataSource lookupDataSource(String aDataSourceName) {
        DataSource dataSourceCache = null;
        try {
            Hashtable parms = new Hashtable();
            parms.put(Context.INITIAL_CONTEXT_FACTORY,
                        "com.ibm.websphere.naming.WsnInitialContextFactory");
            Context ctx = new InitialContext(parms);
            //データソースのルックアップを行う。
            dataSourceCache = (DataSource)ctx.lookup(aDataSourceName);
        } catch (NamingException e) {
            e.printStackTrace();
            UTBLogManager.writeLog(this.getClass(), e.toString());
            return null;
        }
        return dataSourceCache;
    }

// 2005.09.15 add =====>
	/**
	 * 初回限定コネクション(StaleConnectionException対策)をする.
	 *
	 * @return true:正常終了 false:異常終了
	 */
	public boolean executeDummy() throws WICDBException, Exception {
		// 接続用
		Connection con = null;
		boolean bRet = false;			//返り値
		try {
			// Dummy SQL → 本社所属部門リスト取得
			if (getConnectionFtlimitation()) {
				bRet = true;
			}
		} catch (StaleConnectionException se) {
			UTBLogManager.writeLog(this.getClass(), se.toString());
			throw new WICDBException(
				"WIBLoginDB::executeDummy()",
				"90009",se.getMessage(),se.getErrorCode());
		} catch (SQLException e) {
			UTBLogManager.writeLog(this.getClass(), e.toString());
			throw new WICDBException(
				"WIBLoginDB::executeDummy()",
				"90001",
				e.getMessage(),
				e.getErrorCode());
		} catch (Exception e2) {
			UTBLogManager.writeLog("★--Exception:Login");
		}
		return bRet;
	}

	/**
	 * StaleConnectionException」.
	 * <pre>
	 * 指定したデータソースより、DB接続を取得し返す
	 * データソースがキャッシュされていない場合は新規取得する
	 * </pre>
	 *
	 * @return true:正常終了 false:異常終了
	 */
	private boolean getConnectionFtlimitation() throws SQLException {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs   = null;
		StringBuffer sbSql;

		boolean bRet = false;

		try {
			for (int retryCount=2; retryCount>0; retryCount--){
				try {
					//テストモードの場合は直接接続を行う。
					if (!pooling){
						Class.forName( jdbcClassName );
						con = DriverManager.getConnection(jdbcUrl, poolDataSourceUser, poolDataSourcePass);
					//本番モードの場合はコネクションをキャッシュより取得する。
					} else {
						con = poolDataSource.getConnection(poolDataSourceUser, poolDataSourcePass);
						stmt = null;
						sbSql = new StringBuffer();
						sbSql.append(" SELECT ");
						sbSql.append(" * ");
						sbSql.append(" FROM ");
						sbSql.append(poolDataLibrary1);
						sbSql.append(".SG1T ");

						stmt = con.prepareStatement(sbSql.toString());
						rs = stmt.executeQuery();
					}
					// 正常終了
					retryCount = 0;
					//返り値
					bRet = true;
				} catch (StaleConnectionException sce) {
					try {
						if (con != null) {
							try {
								con.close();
							} catch (SQLException se2) {
								UTBLogManager.writeLog(this.getClass(), se2.toString());
								UTBLogManager.writeLog("☆---finally:SQLException");
							} catch (Exception e3) {
								UTBLogManager.writeLog(this.getClass(), e3.toString());
								UTBLogManager.writeLog("☆---finally:Exception");
							}
						}
					} catch (Exception e4) {
						UTBLogManager.writeLog(this.getClass(), e4.toString());
						UTBLogManager.writeLog("☆---finally:Exception2");
					}
				} catch (Exception e2) {
					UTBLogManager.writeLog(this.getClass(), e2.toString());
					throw new SQLException(this.getClass().getName() + 
											"コネクションの取得に失敗しました。1");
				} finally {
					try {
						if (retryCount == 0) {
							closeResultSet(rs);
							closeStatement(stmt);
                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException se2) {
                                    UTBLogManager.writeLog(this.getClass(), se2.toString());
                                    UTBLogManager.writeLog("☆---finally:SQLException");
                                } catch(Exception e3) {
                                    UTBLogManager.writeLog(this.getClass(), e3.toString());
                                    UTBLogManager.writeLog("☆---finally:Exception");
                                }
                            }
                        }
                    } catch (Exception e4) {
                        UTBLogManager.writeLog(this.getClass(), e4.toString());
                        UTBLogManager.writeLog("☆---finally:Exception2");
                    }
                }
            }    // for end
        } catch (Exception e5) {
            UTBLogManager.writeLog(this.getClass(), e5.toString());
            throw new SQLException(this.getClass().getName() + 
                                    "コネクションの取得に失敗しました。2");
        }
        return bRet;
    }
// 2005.09.15 add <=====
}
