package alex.ru.controller;

import alex.ru.annotations.Log;
import alex.ru.dto.MultipartDto;
import alex.ru.exceptions.ReCaptchaException;
import alex.ru.exceptions.UtilException;
import alex.ru.utils.pdf.converter.PdfConverter;
import alex.ru.utils.pdf.converter.PdfOperationFactory;
import alex.ru.utils.recaptcha.ReCaptchaHandler;
import alex.ru.validators.PdfValidator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class PdfController {

    @Log
    private Logger logger;

    private Validator validator;

    private PdfConverter pdfCommonConverter;

    private ReCaptchaHandler reCaptchaHandler;

    private PdfValidator pdfValidator;

    @Autowired
    public PdfController(final PdfConverter pdfCommonConverter,
                         @Qualifier("multipartPdfValidator") final Validator validator,
                         final ReCaptchaHandler reCaptchaHandler,
                         final PdfValidator pdfValidator) {

        this.validator = validator;
        this.pdfCommonConverter = pdfCommonConverter;
        this.reCaptchaHandler = reCaptchaHandler;
        this.pdfValidator = pdfValidator;
    }

    @InitBinder
    public void multiPartInitBinder(final WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping("/pdf")
    public String getPdfView(final Model model) {
        model.addAttribute("multipartDto", new MultipartDto());
        return "pdf";
    }

    @PostMapping("/pdf")
    public String convertPdf(@Validated @ModelAttribute("multipartDto") final MultipartDto dto,
                             final BindingResult bindingResult,
                             final HttpServletRequest request,
                             final HttpServletResponse response) throws IOException {

        if(bindingResult.hasErrors()) {
            return "pdf";
        }
        try {

            final String googleResponse = request.getParameter("g-recaptcha-response");
            reCaptchaHandler.processResponse(googleResponse);

            int operationId;

            if(dto.checkBox.equals("txt"))
                operationId = PdfOperationFactory.POPLER_PDF_TO_TEXT;
            else if(dto.checkBox.equals("html"))
                operationId = PdfOperationFactory.POPLER_PDF_TO_HTML;
            else
                operationId = -1;

            final File resultFile = pdfCommonConverter.convertFileToFile(dto, operationId);

            final InputStream inputStream = new FileInputStream(resultFile);

            response.addHeader("Content-Disposition", "attachment; filename=" + resultFile.getName());
            response.setContentLengthLong(resultFile.length());

            IOUtils.copy(inputStream, response.getOutputStream());

            response.flushBuffer();
            inputStream.close();

            return "pdf";

        } catch (IllegalStateException | UtilException | ReCaptchaException | IOException | NullPointerException e) {

            if(e.getClass().equals(ReCaptchaException.class))
                pdfValidator.validate(e, bindingResult);

            else if(e.getClass().equals(UtilException.class))
                pdfValidator.validate(e, bindingResult);

            return "pdf";
        }
    }
}
