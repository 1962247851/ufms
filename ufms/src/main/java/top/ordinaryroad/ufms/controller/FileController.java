package top.ordinaryroad.ufms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.Constant;
import top.ordinaryroad.ufms.common.utils.ResultTool;

import java.io.File;
import java.io.IOException;

/**
 * @author 19622
 */
@Api
@Controller
@RequestMapping("/api/file")
public class FileController {

    /**
     * 下载文件
     *
     * @param filename 文件名
     * @param uuid     实体类UUID
     * @param type     实体类类型
     * @return ResponseEntity
     */
    @ApiOperation("文件下载")
    @GetMapping(value = "download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> download(@RequestParam String filename, @RequestParam String uuid, @RequestParam(required = false, defaultValue = "file") String type) {
        String filePath = getFullFilePath(type, uuid, filename);
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        try {
            InputStreamResource inputStreamResource = new InputStreamResource(fileSystemResource.getInputStream());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", String.format("attachment; filename=%s", filename));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(fileSystemResource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @param uuid 实体类UUID
     * @param type 实体类类型
     * @return JsonResult
     */
    @ApiOperation("文件上传")
    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResult<?> upload(@RequestParam MultipartFile file, @RequestParam String uuid, @RequestParam(required = false, defaultValue = "file") String type) {
        if (Constant.checkFilePathType(type)) {
            if (file.isEmpty()) {
                return ResultTool.fail();
            }
            String filename = file.getOriginalFilename();
            File dest = new File(getFullFilePath(type, uuid, filename));
            createParentPath(dest);
            try {
                //保存文件
                file.transferTo(dest);
                return ResultTool.success(filename);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultTool.fail();
            }
        } else {
            return ResultTool.fail(ResultCode.PARAM_NOT_VALID);
        }
    }

    private String getFullFilePath(String type, String uuid, String filename) {
        return Constant.getPictureBasePath() + type + File.separatorChar + uuid + File.separatorChar + filename;
    }

    /**
     * 创建父级文件夹
     *
     * @param file 完整路径文件名(注:不是文件夹)
     */
    public static void createParentPath(File file) {
        File parentFile = file.getParentFile();
        // 创建文件夹
        if (parentFile != null && !parentFile.exists() && parentFile.mkdirs()) {
            // 递归创建父级目录
            createParentPath(parentFile);
        }
    }

}