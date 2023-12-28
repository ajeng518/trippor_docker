import { ref } from "vue";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { jwtDecode } from "jwt-decode";

import {
  userConfirm,
  findById,
  tokenRegeneration,
  logout,
  idCheck,
  join,
  updateName,
  updatePwd,
  comparePwd,
  remove,
} from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

export const useMemberStore = defineStore(
  "memberStore",
  () => {
    const router = useRouter();

    const isLogin = ref(false);
    const isLoginError = ref(false);
    const userInfo = ref(null);
    const isValidToken = ref(false);
    const isDuplicate = ref(false);
    const newUser = ref(null);

    const idConfirm = async (userId) => {
      console.log("중복 확인 할 겁니다..." + userId);
      await idCheck(
        userId,
        (response) => {
          console.log(response);
          if (response.status == httpStatusCode.OK) {
            let { data } = response;
            let count = data["userCount"];

            if (count > 0) {
              isDuplicate.value = true;
            } else {
              isDuplicate.value = false;
            }
          }
        },
        (error) => {
          console.error("중복확인 불가능", error);
        }
      );
    };

    const userLogin = async (loginUser) => {
      await userConfirm(
        loginUser,
        (response) => {
          if (response.status === httpStatusCode.CREATE) {
            let { data } = response;
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            isLogin.value = true;
            isLoginError.value = false;
            isValidToken.value = true;
            sessionStorage.setItem("accessToken", accessToken);
            sessionStorage.setItem("refreshToken", refreshToken);
          } else {
            console.log(response);
            isLogin.value = false;
            isLoginError.value = true;
            isValidToken.value = false;
          }
        },
        (error) => {
          console.error("여기 에러!", error);
          isLogin.value = false;
          isLoginError.value = true;
          isValidToken.value = false;
          // console.log("로그인 실패 이유 확인 : ", error.response.data.message);
          if (error.response.status == httpStatusCode.UNAUTHORIZED) {
            alert(error.response.data.message);
          } else if (error.response.status == httpStatusCode.SERVER_ERROR) {
            alert(error.response.data.message);
          }
        }
      );
    };

    const getUserInfo = (token) => {
      let decodeToken = jwtDecode(token);
      findById(
        decodeToken.userId,
        (response) => {
          if (response.status === httpStatusCode.OK) {
            userInfo.value = response.data.userInfo;
            console.log(userInfo.value);
          } else {
            console.log("유저 정보 없음!!!!");
          }
        },
        async (error) => {
          console.error(
            "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
            error.response.status
          );
          // isValidToken.value = false;

          await tokenRegenerate();
        }
      );
    };

    const tokenRegenerate = async () => {
      console.log("토큰 재생성 해주세용가리 ");
      await tokenRegeneration(
        // JSON.stringify(userInfo.value),
        userInfo.value,
        (response) => {
          if (response.status === httpStatusCode.CREATE) {
            console.log("성공했디");
            let accessToken = response.data["access-token"];
            sessionStorage.setItem("accessToken", accessToken);
            isValidToken.value = true;
          }
        },
        async (error) => {
          console.log("토큰 재생성 실패!!!");
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
          if (error.response.status === httpStatusCode.UNAUTHORIZED) {
            // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
            await logout(
              userInfo.value.userid,
              async (response) => {
                if (response.status === httpStatusCode.OK) {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
                isLogin.value = false;
                userInfo.value = null;
                isValidToken.value = false;

                //TODO: 네비게이션 바 변경 해줘야한다...
                // sessionStorage.removeItem("accessToken");
                // sessionStorage.removeItem("refreshToken");
                // localStorage.removeItem("menuStore");
                await userLogout();

                router.push({ name: "user-login" });
              },
              (error) => {
                console.error(error);
                isLogin.value = false;
                userInfo.value = null;
              }
            );
          }
        }
      );
    };

    const userLogout = async () => {
      await logout(
        userInfo.value.userId,
        (response) => {
          if (response.status === httpStatusCode.OK) {
            console.log("로그아웃 왜? ", response);
            isLogin.value = false;
            userInfo.value = null;
            isValidToken.value = false;
            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("refreshToken");
            localStorage.removeItem("menuStore");
          } else {
            console.error("유저 정보 없음!!!!");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    };

    const userJoin = async (newUser) => {
      await join(
        newUser,
        (response) => {
          if (response.status === httpStatusCode.CREATE) {
            alert("회원가입 완료");
            router.push("/user/login"); // 로그인 화면으로 이동
          }
        },
        (error) => {
          console.error(error);
          alert("회원 가입 실패");
        }
      );
    };

    const updateUserName = async (user) => {
      await updateName(
        user,
        (response) => {
          if (response.status == httpStatusCode.OK) {
            console.log("이름수정완료했음", response);
            let { data } = response;
            userInfo.value = data.userInfo;
            alert("회원 이름 수정 완료");
            router.push("/user/mypage");
          }
        },
        (error) => {
          console.error(error);
          alert("회원 정보 수정 실패");
        }
      );
    };

    const updateUserPwd = async (pwd) => {
      const editUser = {
        userId: pwd.userId,
        userPwd: pwd.currentPwd,
      };

      await comparePwd(
        editUser,
        (response) => {
          if (response.status == httpStatusCode.OK) {
            console.log(
              "비밀번호 수정을 위해 현재 비밀번호가 일치하는지 확인함"
            );
            let { data } = response;
            console.log("이제 비밀번호 변경하자!", data);
            //TODO: 비밀번호 변경
            if (data.isCorrectPwd) {
              editUser.userPwd = pwd.newPwd;

              updatePwd(
                editUser,
                (response) => {
                  if (response.status == httpStatusCode.OK) {
                    console.log("비밀번호 수정완료했음", response);
                    let { data } = response;
                    userInfo.value = data.userInfo;
                    alert("비밀번호 회원정보 수정 완료");
                    // router.push("/user/mypage"); // 새로고침 x
                    router.go(0);
                  }
                },
                (error) => {
                  console.error(error);
                  alert("회원 정보 수정 실패");
                }
              );
            } else {
              console.log("현재 비밀번호가 일치하지 않습니다.");
              alert("현재비밀번호를 확인해주세요");
            }
          }
        },
        (error) => {
          console.error(error);
          alert("회원 정보 수정 실패");
        }
      );

      // await updatePwd(
      //   pwd,
      //   (response) => {
      //     if(response.status == httpStatusCode.OK){
      //       console.log("비밀번호 수정완료했음", response);
      //       let { data } = response;
      //       userInfo.value = data.userInfo;
      //       alert("비밀번호 회원정보 수정 완료");
      //       router.push("/user/mypage");
      //     }
      //   },
      //   (error) => {
      //     console.error(error);
      //     alert("회원 정보 수정 실패");
      //   }
      // );
    };

    const removeUser = async (userId) => {
      await remove(
        userId,
        (response) => {
          if (response.status == httpStatusCode.OK) {
            let { data } = response;
            console.log("회원탈퇴 되었습니다.", data);
            if (data.isDelete) {
              alert(
                "회원 탈퇴가 완료되었습니다. 이제는 우리가 헤어져야 할 시간~~ 다음에 또 만나요~~"
              );
              sessionStorage.removeItem("accessToken");
              sessionStorage.removeItem("refreshToken");
              localStorage.removeItem("memberStore");
            }
          }
        },
        async (error) => {
          if (error.response.status == httpStatusCode.UNAUTHORIZED) {
            // 토큰 만료 되었을 때 삭제요청을 하면 로그아웃!
            console.error(
              "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
              error.response.status
            );
            isValidToken.value = false;

            await tokenRegenerate();
          } else {
            console.error("에러 났지롱", error);
          }
        }
      );
    };

    // const findPwdByEmail = async (user) => {
    //   await findPwd(
    //     user,
    //     (response) => {
    //       if(response.status == httpStatusCode.OK){
    //         let {data} = response;
    //         console.log("비밀번호 찾기 완료", data);
    //         router.push();
    //       }
    //     },
    //     (error) => {
    //       console.error(error);
    //       alert("비밀번호 찾기 실패");
    //     }
    //   )
    // };

    return {
      newUser,
      isDuplicate,
      isLogin,
      isLoginError,
      userInfo,
      isValidToken,
      idConfirm,
      userLogin,
      getUserInfo,
      tokenRegenerate,
      userLogout,
      userJoin,
      updateUserName,
      updateUserPwd,
      removeUser,
      // findPwdByEmail,
    };
  },
  { persist: { storage: localStorage } }
);
